package com.example.marvelmvvm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marvelmvvm.data.db.dao.CharacterDao
import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.util.Converters

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
