package com.example.marvelmvvm.data.remote.datasources

import com.example.marvelmvvm.data.remote.responses.CharactersResponse
import com.example.marvelmvvm.data.remote.responses.ComicsResponse
import com.example.marvelmvvm.data.remote.responses.EventsResponse
import com.example.marvelmvvm.data.remote.responses.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelDataSource {

    @GET("characters")
    suspend fun getCharacters(@Query("nameStartsWith") name: String?,@Query("offset") offset: Int,@Query("limit") limit: Int): CharactersResponse

    @GET("characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int): CharactersResponse

    @GET("characters/{characterId}/comics")
    suspend fun getCharacterComics(@Path("characterId") characterId: Int,@Query("offset") offset: Int,@Query("limit") limit: Int): ComicsResponse

    @GET("characters/{characterId}/events")
    suspend fun getCharacterEvents(@Path("characterId") characterId: Int,@Query("offset") offset: Int,@Query("limit") limit: Int): EventsResponse

    @GET("characters/{characterId}/series")
    suspend fun getCharacterSeries(@Path("characterId") characterId: Int,@Query("offset") offset: Int,@Query("limit") limit: Int): SeriesResponse


}