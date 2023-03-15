package co.gabriel.rickyandmorty.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.data.model.Character

class CheckoutViewModel : ViewModel() {
    val listCharacterModel = MutableLiveData<MutableList<Character>>()
    val listBasket = MutableLiveData<Basket>()
    fun listaBasket(basket: Basket) {
        if (basket.listcharacter.isNotEmpty()) listCharacterModel.postValue(basket.listcharacter)
        else listCharacterModel.postValue(mutableListOf<Character>())
    }

    fun getBasket(basket: Basket) {
        if (basket.listcharacter.isNotEmpty()) listBasket.postValue(basket)
        else listBasket.postValue(Basket())
    }
}