package co.gabriel.rickyandmorty.domain

import co.gabriel.rickyandmorty.data.Result
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.data.model.CharacterResponse

interface CharacterRepository {
    suspend fun fetchAllCharactersByPage(page: Int): Result<List<Character>>
}