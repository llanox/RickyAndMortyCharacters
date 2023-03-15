package co.gabriel.rickyandmorty.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.gabriel.rickyandmorty.R
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.databinding.FragmentCheckoutBinding
import co.gabriel.rickyandmorty.ui.viewmodel.CheckoutViewModel
import co.gabriel.rickyandmorty.util.Constants.BASKET
import co.gabriel.rickyandmorty.util.Constants.ERROR_PAY
import co.gabriel.rickyandmorty.util.Constants.TYPE_VIEW_CHECKOUT

class CheckoutFragment : BaseFragment() {
    private lateinit var viewModel: CheckoutViewModel
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter
    private var basket: Basket = Basket()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        kotlin.runCatching {
            basket = arguments?.getSerializable(BASKET) as Basket
            viewModel.listaBasket(basket)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterAdapter = CharacterRecyclerViewAdapter(
            mutableListOf(), binding.tvTotalPrice,
            TYPE_VIEW_CHECKOUT
        )

        binding.characterListRecycle.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.btnGoBack.setOnClickListener {
            goToCharcterListFragment()
        }

        binding.btnPay.setOnClickListener {
            showError(ERROR_PAY)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            goToCharcterListFragment()
        }

        viewModel.listCharacterModel.observe(viewLifecycleOwner) { listCharacter ->
            this.basket.listcharacter = listCharacter
            characterAdapter.updateCharacters(listCharacter)
        }

        viewModel.listBasket.observe(viewLifecycleOwner) { basket ->
            val bundle = Bundle()
            bundle.putSerializable(BASKET, basket)
            findNavController().navigate(
                R.id.action_checkoutFragment_to_CharacterListFragment, bundle
            )
        }
    }

    private fun goToCharcterListFragment() {
        viewModel.getBasket(characterAdapter.getBasket())
    }
}