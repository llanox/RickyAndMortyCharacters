package co.gabriel.rickyandmorty.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.R
import co.gabriel.rickyandmorty.core.doubleToCurrency
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import co.gabriel.rickyandmorty.util.Constants.ALIVE
import co.gabriel.rickyandmorty.util.Constants.DEAD
import co.gabriel.rickyandmorty.util.Constants.FIXEDPRICE
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_20
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_80
import co.gabriel.rickyandmorty.util.Constants.UNKNOWN
import com.bumptech.glide.Glide

class SectionedCharacterAdapter(
    private var items: List<SectionItem> = emptyList(),
    private val tvTotalPrice: TextView,
    private val onCharacterClick: ((Character) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val basket = Basket()

    companion object {
        private const val VIEW_TYPE_HEADER    = 0
        private const val VIEW_TYPE_CHARACTER = 1
    }

    private fun calculatePrice(character: Character): Double =
        when (character.status) {
            ALIVE   -> ((PERCENTAGE_20 + 1) * FIXEDPRICE) * character.quantity
            DEAD    -> (PERCENTAGE_80 * FIXEDPRICE) * character.quantity
            UNKNOWN -> FIXEDPRICE * character.quantity.toDouble()
            else    -> FIXEDPRICE * character.quantity.toDouble()
        }

    private fun updateTotal() {
        val total = items
            .filterIsInstance<SectionItem.CharacterEntry>()
            .map { it.character }
            .filter { it.quantity > 0 }
            .sumOf { calculatePrice(it) }
        tvTotalPrice.text = doubleToCurrency(total)

        //ACTUALIZA EL BASKET

        basket.listcharacters = items
            .filterIsInstance<SectionItem.CharacterEntry>()
            .map { it.character }
            .filter { it.quantity > 0 }
            .toMutableList()
    }

    /* para que el fragment pueda obtener el basket*/

    fun getBasket(): Basket = basket



    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is SectionItem.Header        -> VIEW_TYPE_HEADER
            is SectionItem.CharacterEntry -> VIEW_TYPE_CHARACTER
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val binding = CharacterItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                CharacterViewHolder(binding)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SectionItem.Header -> {
                (holder as HeaderViewHolder).letter.text = item.letter.toString()
            }
            is SectionItem.CharacterEntry -> {
                val vh = holder as CharacterViewHolder
                val character = item.character

                // Nombre y estado
                vh.name.text = character.name
                vh.statusOrPercentage.text = character.status

                // Imagen con Glide
                Glide.with(vh.itemView)
                    .load(character.image)
                    .thumbnail(0.1f)
                    .into(vh.image)

                // Cantidad y precio inicial
                vh.quantity.text = character.quantity.toString()
                vh.price.text = doubleToCurrency(calculatePrice(character))

                // Botones + lógica de add/remove/delete
                vh.btnAdd.setOnClickListener {
                    character.quantity++
                    vh.quantity.text = character.quantity.toString()
                    vh.price.text = doubleToCurrency(calculatePrice(character))
                }
                vh.btnRemove.setOnClickListener {
                    if (character.quantity > 0) {
                        character.quantity--
                        vh.quantity.text = character.quantity.toString()
                        vh.price.text = doubleToCurrency(calculatePrice(character))
                    }
                }
                vh.btnDelete.setOnClickListener {
                    // si quieres mantener botón de borrar, idem aquí
                }
                vh.btnAdd.setOnClickListener {
                    character.quantity++
                    vh.quantity.text = character.quantity.toString()
                    vh.price.text = doubleToCurrency(calculatePrice(character))
                    updateTotal()
                }

                vh.btnRemove.setOnClickListener {
                    if (character.quantity > 0) {
                        character.quantity--
                        vh.quantity.text = character.quantity.toString()
                        vh.price.text = doubleToCurrency(calculatePrice(character))
                        updateTotal()
                    }
                }
            }

        }
    }

    /** Método para actualizar la lista seccionada */
    @Suppress("NotifyDataSetChanged")
    fun updateItems(newItems: List<SectionItem>) {
        this.items = newItems
        notifyDataSetChanged()
        updateTotal()
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val letter: TextView = view.findViewById(R.id.tvSectionHeader)
    }

    inner class CharacterViewHolder(val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Ya tienes acceso a todas estas vistas:
        val image               = binding.characterImage
        val name                = binding.characterName
        val statusOrPercentage  = binding.characterStatusOrPercentage
        val price               = binding.tvPrice
        val quantity            = binding.tvCurrentQuantity
        val btnAdd              = binding.buttonAdd
        val btnRemove           = binding.buttonRemove
        val btnDelete           = binding.buttonDelete
    }

}
