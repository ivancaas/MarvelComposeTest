package com.example.marvelmvvm.di

import com.example.marvelmvvm.nav.MyRouteNavigator
import com.example.marvelmvvm.nav.RouteNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()

    @Provides
    @Named("IO")
    fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO


}