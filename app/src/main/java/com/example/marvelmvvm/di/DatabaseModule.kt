package com.example.marvelmvvm.di

import android.content.Context
import androidx.room.Room
import com.example.marvelmvvm.data.db.AppDatabase
import com.example.marvelmvvm.data.db.dao.CharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "heroes-db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCourseDao(db: AppDatabase): CharacterDao = db.characterDao()


}