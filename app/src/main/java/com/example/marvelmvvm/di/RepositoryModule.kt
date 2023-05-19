package com.example.marvelmvvm.di

import com.example.marvelmvvm.data.db.dao.CharacterDao
import com.example.marvelmvvm.data.remote.datasources.MarvelDataSource
import com.example.marvelmvvm.data.repository.CharacterRepository
import com.example.marvelmvvm.data.repository.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCharactersRepository(
        characterListDatasource: MarvelDataSource,
        characterDao: CharacterDao,
        @Named("IO") dispatcher: CoroutineDispatcher,

    ): CharacterRepository =
        CharacterRepositoryImpl(characterListDatasource,characterDao, dispatcher)

}
