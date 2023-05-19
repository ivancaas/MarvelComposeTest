package com.example.marvelmvvm.data.remote

import com.example.marvelmvvm.data.db.entities.CharacterEntity
import com.example.marvelmvvm.data.remote.responses.CharacterThumbnailResponse
import com.example.marvelmvvm.data.remote.responses.CharactersResponse

fun CharactersResponse.toDomain() =
     data.results.map{
        with(it) {
            CharacterEntity(id, name, description, mapThumbnail(thumbnail),false)
        }
    }

fun mapThumbnail(thumbnail: CharacterThumbnailResponse) =
    "${thumbnail.path.replace("http", "https")}/standard_medium.${thumbnail.extension}"

