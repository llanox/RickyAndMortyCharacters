package co.gabriel.rickyandmorty.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.gabriel.rickyandmorty.core.provideCharacterRepository
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterListFragmentBinding
import co.gabriel.rickyandmorty.data.model.ScreenState
import co.gabriel.rickyandmorty.ui.viewmodel.CharacterListViewModel
import co.gabriel.rickyandmorty.ui.viewmodel.CharacterListViewModelFactory
import androidx.navigation.fragment.findNavController
import co.gabriel.rickyandmorty.R
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.util.Constants.BASKET
import co.gabriel.rickyandmorty.util.Constants.ERROR_BASKET_EMPTY
import co.gabriel.rickyandmorty.util.Constants.TYPE_VIEW_CHARACTER

class CharacterListFragment : BaseFragment() {

    private lateinit var viewModel: CharacterListViewModel
    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.apply {

            val factory =
                CharacterListViewModelFactory(
                    characterRepository = provideCharacterRepository(),
                )
            viewModel = ViewModelProvider(
                owner = this@CharacterListFragment,
                factory = factory
            )[CharacterListViewModel::class.java]
            viewModel.findCharacters().observe(viewLifecycleOwner, Observer(::renderState))

        }

        characterAdapter =
            CharacterRecyclerViewAdapter(mutableListOf(), binding.tvTotalPrice, TYPE_VIEW_CHARACTER)

        binding.characterListRecycle.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.btnBasket.setOnClickListener {
            if (characterAdapter.getBasket().listcharacters.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putSerializable(BASKET, characterAdapter.getBasket())
                findNavController().navigate(
                    R.id.action_CharacterListFragment_to_checkoutFragment,
                    bundle
                )
            } else showError(ERROR_BASKET_EMPTY)
        }

    }

    private fun renderState(screenState: ScreenState<Any>) {
        when (screenState) {
            is ScreenState.Render -> showTeams(screenState.data as MutableList<Character>)
            is ScreenState.Error -> showError(screenState.message)
            is ScreenState.Loading -> showLoading()
        }
    }

    private fun showTeams(list: MutableList<Character>) {
        characterAdapter.updateCharacters(valiteBasketList(list))
    }

    private fun valiteBasketList(list: MutableList<Character>): MutableList<Character> {
        val basketList = arguments?.getSerializable(BASKET) as? Basket
        return if (basketList != null) {
            if (basketList.listcharacters.isNotEmpty()) {
                val listaAll = basketList.listcharacters + list
                val characterList = listaAll.distinctBy { it.id }
                    .groupBy { it.id }
                    .map {
                        it.value.maxBy { character ->
                            basketList.listcharacters.contains(
                                character
                            )
                        }
                    }
                characterList as MutableList<Character>
            } else list
        } else list
    }
}