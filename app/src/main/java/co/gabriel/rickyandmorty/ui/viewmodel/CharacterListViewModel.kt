package co.gabriel.rickyandmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import co.gabriel.rickyandmorty.data.Result
import co.gabriel.rickyandmorty.domain.CharacterRepository
import co.gabriel.rickyandmorty.data.model.Action
import co.gabriel.rickyandmorty.data.model.ScreenState
import co.gabriel.rickyandmorty.util.Constants.DEFAULT_PAGE
import co.gabriel.rickyandmorty.util.Constants.ERROR_FETCHING
import kotlinx.coroutines.Dispatchers

class CharacterListViewModel(private val characterRepository: CharacterRepository) : ViewModel() {

    fun findCharacters(page: Int = DEFAULT_PAGE) = liveData(Dispatchers.IO) {
        emit(ScreenState.Loading())
        when (val result = characterRepository.fetchAllCharactersByPage(page)) {
            is Result.Success -> emit(ScreenState.Render(result.data, Action.RENDER_CHARACTERS))
            is Result.Error -> emit(ScreenState.Error(
                    message = "$ERROR_FETCHING $page",
                    data = result.exception
                )
            )
        }
    }

}

class CharacterListViewModelFactory(private val characterRepository: CharacterRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterListViewModel(
            characterRepository
        ) as T
    }
}