package co.gabriel.rickyandmorty.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import javax.annotation.meta.When

class CharacterRecyclerViewAdapter(
    private var characters: List<Character>,
    private var tvTotalPrice: TextView
) :
    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {

    private val fixedPrice = 12000
    private val ALIVE = "Alive"
    private val DEAD = "Dead"
    private val UNKNOWN = "unknown"
    private val PERCENTAGE_20 = 1.20
    private val PERCENTAGE_80 = 0.80
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.name
        holder.status.text = character.status
        holder.quantity.text = character.quantity.toString()

        Glide.with(holder.itemView)
            .load(character.image)
            .thumbnail(0.1f)
            .into(holder.image)

        holder.btnAdd.setOnClickListener {
            ++character.quantity
            updateFields(holder, position)
        }
        holder.btnRemove.setOnClickListener {
            if (character.quantity > 0) {
                --character.quantity
                updateFields(holder, position)
            }
        }

        holder.price.text = doubleToCurrency(calculatePrice(character))

    }

    private fun updateTotal() {
        var total = 0.0
        characters.forEach { character ->
            total += calculatePrice(character)
        }

        tvTotalPrice.text = doubleToCurrency(total)
    }

    private fun updateFields(holder: ViewHolder, position: Int) {
        val character = characters[position]

        holder.quantity.text = character.quantity.toString()
        holder.price.text = doubleToCurrency(calculatePrice(character))
        updateTotal()
    }

    fun doubleToCurrency(value: Double): String {
        val formatter = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale("es", "ES")))
        return "$" + formatter.format(value)
    }

    private fun calculatePrice(character: Character): Double {
        return when (character.status) {
            ALIVE -> (PERCENTAGE_20 * fixedPrice) * character.quantity
            DEAD -> (PERCENTAGE_80 * fixedPrice) * character.quantity
            UNKNOWN -> fixedPrice * character.quantity.toDouble()
            else -> fixedPrice * character.quantity.toDouble()
        }
    }

    inner class ViewHolder(binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.characterImage
        val name: TextView = binding.characterName
        val price: TextView = binding.tvPrice
        val quantity: TextView = binding.tvCurrentQuantity
        val btnAdd: Button = binding.buttonAdd
        val btnRemove: Button = binding.buttonRemove
        val status: TextView = binding.characterStatus
    }

    fun updateCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

}