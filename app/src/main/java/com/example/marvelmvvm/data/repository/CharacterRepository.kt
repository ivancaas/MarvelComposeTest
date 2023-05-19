package com.example.marvelmvvm.data.repository

import com.example.marvelmvvm.data.db.dao.CharacterDao
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.datasources.MarvelDataSource
import com.example.marvelmvvm.data.remote.responses.ComicsResponse
import com.example.marvelmvvm.data.remote.responses.EventsResponse
import com.example.marvelmvvm.data.remote.responses.SeriesResponse
import com.example.marvelmvvm.data.remote.toDomain
import com.example.marvelmvvm.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CharacterRepository {
    suspend fun getCharacter(characterId: Int): Resource<CharacterEntity>
    suspend fun getCharacters(name:String,page:Int,pageSize: Int = 50) : Resource<List<CharacterEntity>>
    suspend fun getCharacterComics(characterId: Int,page:Int,pageSize: Int = 10) : Resource<ComicsResponse>
    suspend fun getCharacterEvents(characterId: Int,page:Int,pageSize: Int = 10) : Resource<EventsResponse>
    suspend fun getCharacterSeries(characterId: Int,page:Int,pageSize: Int = 10) : Resource<SeriesResponse>
    suspend fun addToFavorites(characterEntity: CharacterEntity)
    suspend fun checkFavorites(characterId: Int):Boolean
    suspend fun deleteFromFavorites(characterId: Int)
}

class CharacterRepositoryImpl @Inject constructor(
    private val marvelDataSource: MarvelDataSource,
    private val characterDao: CharacterDao,
    private val coroutineDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    override suspend fun getCharacter(characterId: Int): Resource<CharacterEntity> = withContext(coroutineDispatcher){
            try {
                val characterResponse = marvelDataSource.getCharacter(characterId)
                val character = characterResponse.toDomain()

                Resource.Success(character.first())
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error in Call")
            }
        }


    override suspend fun getCharacters(name:String,page:Int,pageSize: Int): Resource<List<CharacterEntity>> {
        return withContext(coroutineDispatcher) {
            try {
                val nameQuery = name.ifEmpty { null }
                val charactersResponse = marvelDataSource.getCharacters(name=nameQuery,
                    offset = calculateOffset(page, pageSize),
                    limit = pageSize
                )

                val characters = charactersResponse.toDomain()
                val likedCharacters = characterDao.getAllCharacterIds()
                characters.forEach { character ->
                    if (likedCharacters.indexOf(character.id) != -1)
                        character.liked = true

                }
                Resource.Success(characters)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error in Call")
            }
        }
    }

    override suspend fun getCharacterComics(
        characterId: Int,
        page: Int,
        pageSize: Int
    ): Resource<ComicsResponse> = withContext(coroutineDispatcher) {
        try {
            val comicsResponse = marvelDataSource.getCharacterComics(characterId,
                offset = calculateOffset(page, pageSize),
                limit = pageSize)

            Resource.Success(comicsResponse)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error in Call")
        }
    }

    override suspend fun getCharacterEvents(
        characterId: Int,
        page: Int,
        pageSize: Int
    ): Resource<EventsResponse> = withContext(coroutineDispatcher){
        try {
            val eventsResponse = marvelDataSource.getCharacterEvents(characterId,
                offset = calculateOffset(page, pageSize),
                limit = pageSize)

            Resource.Success(eventsResponse)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error in Call")
        }
    }

    override suspend fun getCharacterSeries(
        characterId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SeriesResponse> = withContext(coroutineDispatcher) {
        try {
            val seriesResponse = marvelDataSource.getCharacterSeries(characterId,
                offset = calculateOffset(page, pageSize),
                limit = pageSize)

            Resource.Success(seriesResponse)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error in Call")
        }
    }

    override suspend fun addToFavorites(characterEntity: CharacterEntity) = withContext(coroutineDispatcher) {
        if(!characterDao.existsCharacter(characterEntity.id))
            characterDao.insert(characterEntity)
    }

    override suspend fun checkFavorites(characterId: Int): Boolean = withContext(coroutineDispatcher) {
        characterDao.existsCharacter(characterId)
    }
    override suspend fun deleteFromFavorites(characterId: Int) = withContext(coroutineDispatcher) {
        if(characterDao.existsCharacter(characterId))
            characterDao.deleteById(characterId)
    }

    private fun calculateOffset (page:Int, pageSize: Int) = page * pageSize
}