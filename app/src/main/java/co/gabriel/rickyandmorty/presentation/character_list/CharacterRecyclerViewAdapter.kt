package co.gabriel.rickyandmorty.presentation.character_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import com.bumptech.glide.Glide

class CharacterRecyclerViewAdapter(private var characters: List<Character>, private var tvTotalPrice: TextView) :
    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {


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
        holder.quantity.text = character.quantity.toString()

        val currentPrice = (12000 * character.quantity)
        holder.price.text = currentPrice.toString()

        Glide.with(holder.itemView)
            .load(character.image)
            .thumbnail(0.1f)
            .into(holder.image)

        holder.btnAdd.setOnClickListener{
            ++character.quantity
            updateFields(holder, position)
        }
        holder.btnRemove.setOnClickListener{
            if (character.quantity > 0){
                --character.quantity
                updateFields(holder, position)
            }
        }
    }

    private fun updateTotal(){
        var total = 0
        characters.forEach { character ->
            total += character.quantity * 12000
        }

        tvTotalPrice.text =  "\$$total"
    }

    private fun updateFields(holder: ViewHolder, position: Int){
        val character = characters[position]
        holder.quantity.text = character.quantity.toString()
        val currentPrice = (12000 * character.quantity)
        holder.price.text = currentPrice.toString()
        updateTotal()
    }


    inner class ViewHolder(binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.characterImage
        val name: TextView = binding.characterName
        val price: TextView = binding.tvPrice
        val quantity: TextView = binding.tvCurrentQuantity
        val btnAdd: Button = binding.buttonAdd
        val btnRemove: Button = binding.buttonRemove
    }

    fun updateCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

}