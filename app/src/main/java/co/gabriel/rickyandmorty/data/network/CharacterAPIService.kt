package co.gabriel.rickyandmorty.data.network

import co.gabriel.rickyandmorty.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterAPIService {
    @GET(".")
    suspend fun fetchAllCharacterByPage(@Query("page") pageNumber: Int = 1): CharacterResponse
}