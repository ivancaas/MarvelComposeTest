package com.example.marvelmvvm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelmvvm.data.db.AppDatabase
import com.example.marvelmvvm.data.db.dao.CharacterDao
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var characterDao: CharacterDao

    @Before
    fun setup() {
        val context = getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        characterDao = database.characterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testGetAllCharacters() = runBlocking {
        // Arrange
        val characters = mockCharacterEntities
        characterDao.insertAll(characters)

        // Act
        val result = characterDao.getAllCharacters()

        // Assert
        assertEquals(characters, result)
    }

    @Test
    fun testGetCharactersByName() {
        // Arrange

        characterDao.insertAll(mockCharacterEntities)

        // Act
        val result = characterDao.getCharactersByName(mockCharacterEntities.first().name)

        // Assert
        assertEquals(mockCharacterEntities.first(), result)
    }

    @Test
    fun testInsertAndDeleteCharacter() {
        // Arrange
        val character = mockCharacterEntities.first()

        // Act
        characterDao.insert(character)
        val existsBeforeDeletion = characterDao.existsCharacter(character.id)
        characterDao.deleteById(character.id)
        val existsAfterDeletion = characterDao.existsCharacter(character.id)

        // Assert
        assertTrue(existsBeforeDeletion)
        assertFalse(existsAfterDeletion)
    }

    @Test
    fun testGetAllCharacterIds() {

        characterDao.insertAll(mockCharacterEntities)

        // Act
        val result = characterDao.getAllCharacterIds()

        // Assert
        assertEquals(listOf(1, 2, 3), result)
    }
}

