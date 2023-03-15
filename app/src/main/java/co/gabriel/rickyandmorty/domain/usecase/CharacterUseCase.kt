package co.gabriel.rickyandmorty.domain.usecase

import androidx.lifecycle.liveData
import co.gabriel.rickyandmorty.data.Result
import co.gabriel.rickyandmorty.domain.CharacterRepository
import co.gabriel.rickyandmorty.presentation.Action
import co.gabriel.rickyandmorty.presentation.ScreenState
import co.gabriel.rickyandmorty.ui.viewmodel.DEFAULT_PAGE
import kotlinx.coroutines.Dispatchers


class CharacterUseCase(private val characterRepository: CharacterRepository)  {

    fun findCharacters(page : Int = DEFAULT_PAGE) =  liveData(Dispatchers.IO) {
        emit(ScreenState.Loading())
        when(val result = characterRepository.fetchAllCharactersByPage(page)){
            is Result.Success ->  emit(ScreenState.Render(result.data, Action.RENDER_CHARACTERS))
            is Result.Error -> emit(ScreenState.Error(message = "Error fetching characters for the page $page",data = result.exception))
        }
    }
}