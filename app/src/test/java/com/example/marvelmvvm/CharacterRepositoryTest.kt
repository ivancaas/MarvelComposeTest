package com.example.marvelmvvm

import com.example.marvelmvvm.data.db.dao.CharacterDao
import com.example.marvelmvvm.data.remote.datasources.MarvelDataSource
import com.example.marvelmvvm.data.remote.responses.CharactersDataResponse
import com.example.marvelmvvm.data.remote.responses.CharactersResponse
import com.example.marvelmvvm.data.repository.CharacterRepositoryImpl
import com.example.marvelmvvm.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterRepositoryTest {


    @Mock
    private lateinit var marvelDataSource: MarvelDataSource

    @Mock
    private lateinit var characterDao: CharacterDao

    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        repository = CharacterRepositoryImpl(marvelDataSource, characterDao, Dispatchers.Unconfined)
    }

    @Test
    fun `getCharacters returns Success`() = runBlocking {
        // Arrange
        val charactersResponse = CharactersResponse(CharactersDataResponse())
        whenever(marvelDataSource.getCharacters(null, 0, 50)).thenReturn(charactersResponse)

        // Act
        val result = repository.getCharacters("", 0)

        // Assert
        verify(marvelDataSource).getCharacters(null, 0, 50)
        assert(result is Resource.Success)
    }


    @Test
    fun `getCharacters returns Error and throws Exception`() = runBlocking {
        // Arrange
        whenever(marvelDataSource.getCharacters(null, 0, 50)).thenThrow(MockitoException::class.java)

        // Act
        val result = repository.getCharacters("", 0)

        // Assert
        verify(marvelDataSource).getCharacters(null, 0, 50)
        assert(result is Resource.Error)
    }

    @Test
    fun `getCharacter returns Success when data source returns valid response`() = runBlocking {
        // Arrange
        val characterId = 123
        val characterResponse = mockCharList.first()
        val expectedCharacterEntity = mockCharacterEntities.first()
        whenever(marvelDataSource.getCharacter(characterId)).thenReturn(mockharacterResponse)

        // Act
        val result = repository.getCharacter(characterId)

        // Assert
        verify(marvelDataSource).getCharacter(characterId)
        assertTrue(result is Resource.Success)
        assertEquals(expectedCharacterEntity, (result as Resource.Success).data)
    }


    @Test
    fun `getCharacter returns Error when data source throws exception`() = runBlocking {
        // Arrange
        val characterId = 123
        val errorMessage = "Error in Call" // DEFAULT ERROR
        whenever(marvelDataSource.getCharacter(characterId)).thenThrow(MockitoException::class.java)

        // Act
        val result = repository.getCharacter(characterId)

        // Assert
        verify(marvelDataSource).getCharacter(characterId)
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

}
