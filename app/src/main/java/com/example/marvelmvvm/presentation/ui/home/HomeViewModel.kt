package com.example.marvelmvvm.presentation.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.repository.CharacterRepository
import com.example.marvelmvvm.nav.RouteNavigator
import com.example.marvelmvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val characterRepository: CharacterRepository
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var page = 0
    var searchQuery = mutableStateOf("")
    var noMoreResults by mutableStateOf(false)
    var characters = mutableStateListOf<CharacterEntity>()
    private var searchJob: Job? = null
    init {
        getCharacters()
    }

    fun onSearchQueryChanged(newValue:String){
        searchQuery.value = newValue.replace("\n","")

        searchJob?.cancel() // cancelamos el trabajo actual si existe
        searchJob = viewModelScope.launch {
            delay(1000)
            page = 0
        noMoreResults = false
        characters.clear()
        getCharacters()
        }
    }
    fun addOrRemoveFromFavs(character: CharacterEntity){
        viewModelScope.launch {
        if (character.liked) characterRepository.deleteFromFavorites(character.id)
        else characterRepository.addToFavorites(character)
        }
    }
    suspend fun checkFavorite(characterId: Int):Boolean = characterRepository.checkFavorites(characterId)


    fun getCharacters(){
        if (!noMoreResults) {
            viewModelScope.launch {
                val response = characterRepository.getCharacters(searchQuery.value,page)

                when (response){
                    is Resource.Success ->{
                    response.data?.let {
                        if (page == 0 && it.isEmpty()) _uiState.update { HomeUiState.EmptyList }

                        if (page == 0) characters.clear()
                        if (it.isEmpty()) noMoreResults = true
                        else {
                            characters.addAll(it)
                            _uiState.update { currentState ->
                                if (currentState !is HomeUiState.Success) HomeUiState.Success
                                else currentState
                            }
                            page++
                        }
                    }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            HomeUiState.Error(response.message ?: "Error")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Error(val message:String) : HomeUiState()
    object EmptyList : HomeUiState()
    object Success : HomeUiState()
//  data class Success(val characterList: String) : HomeUiState()
    //Another approach
}
