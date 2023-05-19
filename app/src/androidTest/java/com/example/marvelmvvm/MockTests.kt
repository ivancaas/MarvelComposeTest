package com.example.marvelmvvm

import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.responses.CharacterResponse
import com.example.marvelmvvm.data.remote.responses.CharacterThumbnailResponse
import com.example.marvelmvvm.data.remote.responses.CharactersDataResponse
import com.example.marvelmvvm.data.remote.responses.CharactersResponse

val mockThumbnail = CharacterThumbnailResponse("pathimage", "jpg")


val mockCharList = listOf(
    CharacterResponse("", 1, "hero1", mockThumbnail),
    CharacterResponse("", 2, "hero2", mockThumbnail),
    CharacterResponse("", 3, "hero3", mockThumbnail),
)
val mockCharacterEntities = listOf(
    CharacterEntity(1, "hero1", "desc", "testimage.jpg",false),
    CharacterEntity(2, "hero2", "desc", "testimage.jpg",false),
    CharacterEntity(3, "hero3", "desc", "testimage.jpg",false),)

val mockcharacterResponse = CharactersResponse(CharactersDataResponse(results = mockCharList))



