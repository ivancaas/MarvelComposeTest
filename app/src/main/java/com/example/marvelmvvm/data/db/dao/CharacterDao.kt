package com.example.marvelmvvm.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.marvelmvvm.data.db.entities.CharacterEntity

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    fun getCharactersByName(name: String): CharacterEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(courses: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(courses: List<CharacterEntity>)

    @Query("DELETE FROM characters")
    fun deleteAll()

    @Query("DELETE FROM characters WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM characters WHERE id = :id)")
    fun existsCharacter(id: Int): Boolean

    @Query("SELECT id FROM characters")
    fun getAllCharacterIds(): List<Int>

}
