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
import java.util.Locale
import androidx.appcompat.widget.SearchView


class CharacterListFragment : BaseFragment() {
    // Guarda la lista original para busquedas posteriores

    private lateinit var originalList: List<Character>
    // Nuevo adapter para la lista seccionada
    private lateinit var sectionedAdapter: SectionedCharacterAdapter


    private lateinit var viewModel: CharacterListViewModel
    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!

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

        // 1) Inicializa el adapter de secciones
        sectionedAdapter = SectionedCharacterAdapter(
            items = emptyList(),
            tvTotalPrice = binding.tvTotalPrice
        )

                    binding.characterListRecycle.apply {
                adapter = sectionedAdapter
                layoutManager = LinearLayoutManager(context)
            }

        //Listener sobre SearchView

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = false
            override fun onQueryTextChange(text: String): Boolean {
                //Filtramos la lista original
                val filtered = originalList
                    .filter { it.name.contains(text, ignoreCase = true) }

                if (filtered.isEmpty()) {
                    // Sin resultados
                    binding.characterListRecycle.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    // Hay resultados
                    binding.tvEmptyState.visibility = View.GONE
                    binding.characterListRecycle.visibility = View.VISIBLE
                }

                //Actualizamos secciones con el filtrado

                val items = sectionedList(filtered)
                sectionedAdapter.updateItems(items)
                return true
            }
        })

        binding.btnBasket.setOnClickListener {
            val currentBasket = sectionedAdapter.getBasket()
            if (currentBasket.listcharacters.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putSerializable(BASKET, currentBasket)
                }
                findNavController().navigate(
                    R.id.action_CharacterListFragment_to_checkoutFragment,
                    bundle
                )

            }else showError(ERROR_BASKET_EMPTY)
        }



    }

    private fun renderState(screenState: ScreenState<Any>) {
        when (screenState) {
            is ScreenState.Render-> {
            val list = screenState.data as MutableList<Character>
            originalList = list
            showSectionedList(list)

            }
            is ScreenState.Error  -> showError(screenState.message)
            is ScreenState.Loading-> showLoading()

        }
    }

    //Metodo para generar la lista seleccionada

    private fun sectionedList(list: List<Character>): List<SectionItem> {
        return list
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
            .groupBy {
                // Usa '#' para indefinidos, o adapta a tu criterio
                it.name.firstOrNull()?.uppercaseChar() ?: '#'
            }
            .flatMap { (letter, chars) ->
                listOf(SectionItem.Header(letter)) +
                        chars.map { SectionItem.CharacterEntry(it) }
            }
    }


    // 3) Reemplaza showTeams por showSectionedList
    private fun showSectionedList(list: List<Character>) {
        val items = sectionedList(list)
        sectionedAdapter.updateItems(items)
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