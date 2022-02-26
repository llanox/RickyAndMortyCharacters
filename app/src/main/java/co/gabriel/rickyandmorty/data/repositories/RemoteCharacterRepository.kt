package co.gabriel.rickyandmorty.data.repositories

import co.gabriel.rickyandmorty.data.Result
import co.gabriel.rickyandmorty.data.network.CharacterAPIService
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.domain.CharacterRepository
import timber.log.Timber

class RemoteCharacterRepository(private val characterAPIService: CharacterAPIService) : CharacterRepository {
    override suspend fun fetchAllCharactersByPage(page: Int): Result<List<Character>> {
        return try {
            Result.Success(characterAPIService.fetchAllCharacterByPage(pageNumber = page).results)
        } catch (ex: Throwable) {
            Timber.e(ex, "Error fetching list of characters for page $page")
            Result.Error(exception = ex)
        }
    }
}