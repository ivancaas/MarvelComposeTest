package com.example.marvelmvvm.presentation.ui.detail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.responses.ComicsResponse
import com.example.marvelmvvm.data.remote.responses.EventsResponse
import com.example.marvelmvvm.data.remote.responses.SeriesResponse
import com.example.marvelmvvm.data.repository.CharacterRepository
import com.example.marvelmvvm.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val routeNavigator: RouteNavigator,
    private val characterRepository: CharacterRepository
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()
    val comicUiState = MutableStateFlow<ComicUiState>(ComicUiState.Loading)
    val eventUiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val serieUiState = MutableStateFlow<SeriesUiState>(SeriesUiState.Loading)
    private var comicPage = 0
    private var eventPage = 0
    private var seriePage = 0
    var noMoreComics by mutableStateOf(false)
    var noMoreEvents by mutableStateOf(false)
    var noMoreSeries by mutableStateOf(false)


    val characterId = mutableStateOf("")

    init {
    }

    fun addOrRemoveFromFavs(character: CharacterEntity) {
        viewModelScope.launch {
            if (character.liked) characterRepository.deleteFromFavorites(character.id)
            else characterRepository.addToFavorites(character)
        }
    }

    fun getCharacterData() {
        viewModelScope.launch {
            characterRepository.getCharacter(characterId.value.toInt()).data?.let { characterEntity ->
                _uiState.update {
                    DetailUiState.Success(characterEntity)
                }
                getCharacterComic()
                getCharacterEvents()
                getCharacterSeries()
            }
        }
    }

    fun getCharacterComic() {
        viewModelScope.launch {
            if (!noMoreComics) {
                val response = characterRepository.getCharacterComics(
                    characterId.value.toInt(),
                    comicPage
                )
                response.data?.comicResponse?.results?.let {
                    if (it.isEmpty()) noMoreComics = true

                    comicUiState.update { currentState ->

                        if (currentState is ComicUiState.Success) {
                            ComicUiState.Success(currentState.comics.plus(it))
                        } else ComicUiState.Success(it)
                    }
                    comicPage++
                }
                if (response.message != null){
                    comicUiState.update {
                        ComicUiState.Error
                    }
                }
            }
        }
    }

    fun getCharacterEvents() {
        viewModelScope.launch {
            if (!noMoreEvents) {
                val response = characterRepository.getCharacterEvents(
                    characterId.value.toInt(),
                    eventPage
                )
                response.data?.eventResponse?.eventList?.let {
                    if (it.isEmpty()) noMoreEvents = true
                    eventUiState.update { currentState ->

                        if (currentState is EventUiState.Success) {
                            EventUiState.Success(currentState.events.plus(it))
                        } else EventUiState.Success(it)
                    }
                    eventPage++
                }
                if (response.message != null){
                    eventUiState.update {
                        EventUiState.Error
                    }
                }
            }
        }
    }

    fun getCharacterSeries() {
        viewModelScope.launch {
            if (!noMoreSeries) {
                val response = characterRepository.getCharacterSeries(
                    characterId.value.toInt(),
                    seriePage
                )
                response.data?.serieResponse?.serieList?.let {
                    if (it.isEmpty()) noMoreSeries = true
                    serieUiState.update { currentState ->

                        if (currentState is SeriesUiState.Success) {
                            SeriesUiState.Success(currentState.series.plus(it))
                        } else SeriesUiState.Success(it)
                    }
                    seriePage++
                }
                if (response.message != null){
                    serieUiState.update {
                            SeriesUiState.Error
                    }
                }
            }
        }
    }

}

sealed class DetailUiState {
    object Loading : DetailUiState()
    object Error : DetailUiState()
    data class Success(val characterData: CharacterEntity) : DetailUiState()

}

sealed class EventUiState {
    object Loading : EventUiState()
    object Error : EventUiState()
    data class Success(val events: List<EventsResponse.EventResponse.EventData>) : EventUiState()
}

sealed class ComicUiState {
    object Loading : ComicUiState()
    object Error : ComicUiState()
    data class Success(val comics: List<ComicsResponse.ComicResponse.ComicData>) : ComicUiState()
}

sealed class SeriesUiState {
    object Loading : SeriesUiState()
    object Error : SeriesUiState()
    data class Success(val series: List<SeriesResponse.SerieResponse.SerieData>) : SeriesUiState()
}
