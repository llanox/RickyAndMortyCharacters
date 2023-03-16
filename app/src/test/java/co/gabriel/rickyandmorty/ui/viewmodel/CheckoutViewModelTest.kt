package co.gabriel.rickyandmorty.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.gabriel.rickyandmorty.data.model.Basket
import org.junit.Before
import org.junit.Test
import co.gabriel.rickyandmorty.data.model.Character
import org.junit.Rule

class CheckoutViewModelTes {

    private lateinit var checkoutViewModel: CheckoutViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        checkoutViewModel = CheckoutViewModel()
    }

    @Test
    fun `test listBasket with non-empty list`() {

        val mutableList:MutableList<Character> = mutableListOf()
        mutableList.add(Character(id="1", name = "yenifer",image="image", status = "status"))

        val basket = Basket(listcharacters = mutableList)
        checkoutViewModel.listaBasket(basket)

        assert(basket.listcharacters == checkoutViewModel.listCharacterModel.value)
    }

    @Test
    fun `test listBasket with empty list`() {
        val mutableList:MutableList<Character> = mutableListOf()
        val basket = Basket(mutableList)
        checkoutViewModel.listaBasket(basket)

        assert( mutableListOf<Character>() == checkoutViewModel.listCharacterModel.value)
    }

    @Test
    fun `test getBasket with non-empty list`() {

        val mutableList:MutableList<Character> = mutableListOf()
        mutableList.add(Character(id="1", name = "yenifer",image="image", status = "status"))

        val basket = Basket(listcharacters = mutableList)
        checkoutViewModel.getBasket(basket)

        assert(basket == checkoutViewModel.listBasket.value)
    }

    @Test
    fun `test getBasket with empty list`() {
        val mutableList:MutableList<Character> = mutableListOf()
        val basket = Basket(mutableList)
        checkoutViewModel.getBasket(basket)

        assert( Basket() == checkoutViewModel.listBasket.value)
    }
}