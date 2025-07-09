package co.gabriel.rickyandmorty.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.R
import co.gabriel.rickyandmorty.core.doubleToCurrency
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.data.model.Basket
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import co.gabriel.rickyandmorty.util.Constants.ALIVE
import co.gabriel.rickyandmorty.util.Constants.DEAD
import co.gabriel.rickyandmorty.util.Constants.FIXEDPRICE
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_0_STATUS
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_20
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_20_STATUS
import co.gabriel.rickyandmorty.util.Constants.PERCENTAGE_80
import co.gabriel.rickyandmorty.util.Constants.TYPE_VIEW_CHECKOUT
import co.gabriel.rickyandmorty.util.Constants.UNKNOWN
import com.bumptech.glide.Glide
import java.util.*

class CharacterRecyclerViewAdapter(
    private var characters: MutableList<Character>,
    private var tvTotalPrice: TextView,
    private var typeView: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = listOf()
    private var basket = Basket()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CHARACTER = 1
    }

    init {
        groupCharacters()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_CHARACTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_item, parent, false)
            HeaderViewHolder(view)
        } else {
            val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CharacterViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Character -> (holder as CharacterViewHolder).bind(item, position)
            is String -> (holder as HeaderViewHolder).bind(item)
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerText: TextView = view.findViewById(R.id.headerText)
        fun bind(title: String) {
            headerText.text = title
        }
    }

    inner class CharacterViewHolder(val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, position: Int) {
            binding.characterName.text = character.name
            binding.tvCurrentQuantity.text = character.quantity.toString()
            binding.characterStatusOrPercentage.text = validateStatusOrPercentage(character.status)

            activateDeleteButton(character, this)

            Glide.with(binding.root)
                .load(character.image)
                .thumbnail(0.1f)
                .into(binding.characterImage)

            binding.buttonAdd.setOnClickListener {
                ++character.quantity
                updateFields(this, character)
            }

            binding.buttonRemove.setOnClickListener {
                if (character.quantity > 0) {
                    --character.quantity
                    updateFields(this, character)
                }
            }

            binding.buttonDelete.setOnClickListener {
                characters.remove(character)
                groupCharacters()
                updateTotal()
            }

            binding.tvPrice.text = doubleToCurrency(calculatePrice(character))
            updateTotal()
        }
    }

    private fun updateFields(holder: CharacterViewHolder, character: Character) {
        holder.binding.tvCurrentQuantity.text = character.quantity.toString()
        holder.binding.tvPrice.text = doubleToCurrency(calculatePrice(character))
        activateDeleteButton(character, holder)
        updateTotal()
    }

    private fun activateDeleteButton(character: Character, holder: CharacterViewHolder) {
        if (character.quantity == 1 && typeView == TYPE_VIEW_CHECKOUT) {
            holder.binding.buttonRemove.visibility = View.GONE
            holder.binding.buttonDelete.visibility = View.VISIBLE
        } else {
            holder.binding.buttonRemove.visibility = View.VISIBLE
            holder.binding.buttonDelete.visibility = View.GONE
        }
    }

    private fun validateStatusOrPercentage(status: String): String {
        return if (typeView == TYPE_VIEW_CHECKOUT) {
            when (status) {
                ALIVE -> "+$PERCENTAGE_20_STATUS"
                DEAD -> "-$PERCENTAGE_20_STATUS"
                UNKNOWN -> PERCENTAGE_0_STATUS
                else -> PERCENTAGE_0_STATUS
            }
        } else status
    }

    private fun calculatePrice(character: Character): Double {
        return when (character.status) {
            ALIVE -> ((PERCENTAGE_20 + 1) * FIXEDPRICE) * character.quantity
            DEAD -> (PERCENTAGE_80 * FIXEDPRICE) * character.quantity
            UNKNOWN -> FIXEDPRICE * character.quantity.toDouble()
            else -> FIXEDPRICE * character.quantity.toDouble()
        }
    }

    private fun updateTotal() {
        val total = characters.sumOf { calculatePrice(it) }
        tvTotalPrice.text = doubleToCurrency(total)
        basket.listcharacters = characters.filter { it.quantity > 0 } as MutableList<Character>
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCharacters(characters: MutableList<Character>) {
        this.characters = characters
        groupCharacters()
        notifyDataSetChanged()
    }

    fun getBasket(): Basket = basket

    private fun groupCharacters() {
        val grouped = characters.groupBy { it.status }
        val sortedStatuses = listOf(ALIVE, DEAD, UNKNOWN)

        val result = mutableListOf<Any>()
        for (status in sortedStatuses) {
            val list = grouped[status]?.sortedBy { it.name }
            if (!list.isNullOrEmpty()) {
                result.add(status)
                result.addAll(list)
            }
        }

        items = result
    }
}