package com.example.marvelmvvm.data.remote.responses



data class CharactersResponse(
    val data: CharactersDataResponse,
)

data class CharactersDataResponse(
    val results: List<CharacterResponse> = emptyList(),
)

data class CharacterResponse(
    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: CharacterThumbnailResponse,
)

data class CharacterThumbnailResponse(
    val extension: String, // jpg
    val path: String // http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available
)

