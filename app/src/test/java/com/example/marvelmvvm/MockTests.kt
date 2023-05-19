package com.example.marvelmvvm

import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.mapThumbnail
import com.example.marvelmvvm.data.remote.responses.CharacterResponse
import com.example.marvelmvvm.data.remote.responses.CharacterThumbnailResponse
import com.example.marvelmvvm.data.remote.responses.CharactersDataResponse
import com.example.marvelmvvm.data.remote.responses.CharactersResponse

val mockThumbnail = CharacterThumbnailResponse("pathimage", "jpg")


val mockCharList = listOf(
    CharacterResponse("desc", 1, "hero1", mockThumbnail),
    CharacterResponse("desc", 2, "hero2", mockThumbnail),
    CharacterResponse("desc", 3, "hero3", mockThumbnail),
)
val mockCharacterEntities = listOf(
    CharacterEntity(1, "hero1", "desc", mapThumbnail( mockThumbnail),false),
    CharacterEntity(2, "hero2", "desc", mapThumbnail( mockThumbnail),false),
    CharacterEntity(3, "hero3", "desc", mapThumbnail( mockThumbnail),false),)

val mockharacterResponse = CharactersResponse(CharactersDataResponse(results = mockCharList))



