package com.example.marvelmvvm.data.remote.responses


import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("data")
    val eventResponse: EventResponse
) {
    data class EventResponse(
        @SerializedName("results")
        val eventList: List<EventData>
    ) {
        data class EventData(
            @SerializedName("id")
            val id: Int, // 116
            @SerializedName("title")
            val title: String, // Acts of Vengeance!
            @SerializedName("description")
            val description: String, // Loki sets about convincing the super-villains of Earth to attack heroes other than those they normally fight in an attempt to destroy the Avengers to absolve his guilt over inadvertently creating the team in the first place.
            @SerializedName("start")
            val start: String, // 1989-12-10 00:00:00
            @SerializedName("end")
            val end: String, // 2008-01-04 00:00:00
            @SerializedName("thumbnail")
            val thumbnail: CharacterThumbnailResponse,
            @SerializedName("creators")
            val creators: Creators,

            ) {




            data class Creators(
                @SerializedName("available")
                val available: Int, // 102
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/events/116/creators
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 20
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/creators/2707
                    @SerializedName("name")
                    val name: String, // Jeff Albrecht
                    @SerializedName("role")
                    val role: String // inker
                )
            }

            data class Characters(
                @SerializedName("available")
                val available: Int, // 108
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/events/116/characters
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 20
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/characters/1009435
                    @SerializedName("name")
                    val name: String // Alicia Masters
                )
            }

            data class Stories(
                @SerializedName("available")
                val available: Int, // 161
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/events/116/stories
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 20
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/stories/12960
                    @SerializedName("name")
                    val name: String, // Fantastic Four (1961) #334
                    @SerializedName("type")
                    val type: String // cover
                )
            }

            data class Comics(
                @SerializedName("available")
                val available: Int, // 53
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/events/116/comics
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 20
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/comics/12744
                    @SerializedName("name")
                    val name: String // Alpha Flight (1983) #79
                )
            }

            data class Series(
                @SerializedName("available")
                val available: Int, // 22
                @SerializedName("collectionURI")
                val collectionURI: String, // http://gateway.marvel.com/v1/public/events/116/series
                @SerializedName("items")
                val items: List<Item>,
                @SerializedName("returned")
                val returned: Int // 20
            ) {
                data class Item(
                    @SerializedName("resourceURI")
                    val resourceURI: String, // http://gateway.marvel.com/v1/public/series/2116
                    @SerializedName("name")
                    val name: String // Alpha Flight (1983 - 1994)
                )
            }

            data class Next(
                @SerializedName("resourceURI")
                val resourceURI: String, // http://gateway.marvel.com/v1/public/events/240
                @SerializedName("name")
                val name: String // Days of Future Present
            )

            data class Previous(
                @SerializedName("resourceURI")
                val resourceURI: String, // http://gateway.marvel.com/v1/public/events/233
                @SerializedName("name")
                val name: String // Atlantis Attacks
            )
        }
    }
}