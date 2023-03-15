package co.gabriel.rickyandmorty.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.databinding.FragmentCheckoutBinding
import co.gabriel.rickyandmorty.ui.viewmodel.CheckoutViewModel
import co.gabriel.rickyandmorty.util.Constants
import co.gabriel.rickyandmorty.util.Constants.TYPE_VIEW_CHECKOUT

class CheckoutFragment : Fragment() {
    private lateinit var viewModel: CheckoutViewModel
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckoutViewModel::class.java)

        val lista = arguments?.getSerializable(Constants.BASKET) as Basket

        characterAdapter = CharacterRecyclerViewAdapter(
            mutableListOf(), binding.tvTotalPrice,
            TYPE_VIEW_CHECKOUT
        )

        binding.characterListRecycle.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }

        characterAdapter.updateCharacters(lista.listcharacter)

    }

}