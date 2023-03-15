package co.gabriel.rickyandmorty.data.model

import com.google.gson.annotations.SerializedName

data class Character(
    var quantity: Int = 0,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("status") val status: String,
)

data class Info(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)

data class CharacterResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Character> = emptyList()
)
