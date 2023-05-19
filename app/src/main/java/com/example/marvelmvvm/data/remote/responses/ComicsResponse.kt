package com.example.marvelmvvm.data.remote.responses


import com.google.gson.annotations.SerializedName

data class ComicsResponse(

    @SerializedName("data")
    val comicResponse: ComicResponse
) {
    data class ComicResponse(
        @SerializedName("results")
        val results: List<ComicData>
    ) {
        data class ComicData(
            @SerializedName("id")
            val id: Int, // 27238
            @SerializedName("digitalId")
            val digitalId: Int, // 0
            @SerializedName("title")
            val title: String, // Wolverine Saga (2009) #7
            @SerializedName("description")
            val description: String?, // Collects Iron Man (2020) #20-25. Tony Stark is ready to re-engage! With Korvac, the Power Cosmic and a nasty morphine addiction behind him, Tony re-enters the world. But things don't go exactly as planned, and Iron Man soon finds himself battling a hyper-intelligent gorilla through the streets of New York! Then, a trip to California leaves Tony stranded in the southwestern desert with malfunctioning armor - and things go downhill from there! Shellhead faces deadly new villain Switchback, classic foe the Titanium Man and a dangerous conspiracy involving the Mandarin's Rings! To track down these deadly weapons, he'll have to employ stealth mode! Christopher Cantwell concludes his thought-provoking run with a shocking question: Must there be an Iron Man?!
            @SerializedName("modified")
            val modified: String, // -0001-11-30T00:00:00-0500
            @SerializedName("dates")
            val dates: List<Date>,
            @SerializedName("prices")
            val prices: List<Price>,
            @SerializedName("thumbnail")
            val thumbnail: CharacterThumbnailResponse,
            @SerializedName("images")
            val images: List<Image>,
            @SerializedName("creators")
            val creators: Creators,
        ) {

            data class Series(
                @SerializedName("resourceURI")
                val resourceURI: String, // http://gateway.marvel.com/v1/public/series/8086
                @SerializedName("name")
                val name: String // Wolverine Saga (2009)
            )

            data class Date(
                @SerializedName("type")
                val type: String, // onsaleDate
                @SerializedName("date")
                val date: String // 2029-12-31T00:00:00-0500
            )

            data class Price(
                @SerializedName("type")
                val type: String, // printPrice
                @SerializedName("price")
                val price: Double // 19.99
            )

            data class Image(
                @SerializedName("path")
                val path: String, // http://i.annihil.us/u/prod/marvel/i/mg/c/80/63ece617db08e
                @SerializedName("extension")
                val extension: String // jpg
            )

            data class Creators(
                @SerializedName("available")
                val available: Int, // 1
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/comics/27238/creators
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 1
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/creators/4430
                    @SerializedName("name")
                    val name: String, // Jeff Youngquist
                    @SerializedName("role")
                    val role: String // editor
                )
            }
        }
    }
}