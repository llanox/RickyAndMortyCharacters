package co.gabriel.rickyandmorty.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Basket(
    @SerializedName("listcharacter") var listcharacter: MutableList<Character> = mutableListOf()
): Serializable
