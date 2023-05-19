package com.example.marvelmvvm.di

import com.example.marvelmvvm.data.remote.datasources.MarvelDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideCharactersDataSource(retrofit: Retrofit): MarvelDataSource = retrofit.create(MarvelDataSource::class.java)

}
