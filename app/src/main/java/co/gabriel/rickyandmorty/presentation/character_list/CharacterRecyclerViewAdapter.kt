package co.gabriel.rickyandmorty.presentation.character_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import com.bumptech.glide.Glide

class CharacterRecyclerViewAdapter(private var characters : List<Character>, private val itemClickListener: OnClickListener ) :
    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    interface OnClickListener {
        fun onButtonAddClick(
            price: Int
        )
        fun onButtonRemoveClick(
            price: Int,
            subtotal: Int
        )
    }

    private fun validateSubtotal(subtotal: Int): Boolean {
        return subtotal != 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.name
        val price = 10000
        var totalItem: Int = holder.adder.text.toString().toInt()
        var subtotal: Int = holder.subtotal.text.toString().toInt()
        holder.price.text = price.toString()
        holder.add.setOnClickListener{
            subtotal += price
            totalItem += 1
            itemClickListener.onButtonAddClick(price)
            holder.subtotal.text = subtotal.toString()
            holder.adder.text = totalItem.toString()
        }
        holder.remove.setOnClickListener {
            if (validateSubtotal(subtotal)){
                itemClickListener.onButtonRemoveClick(price, subtotal)
                subtotal -= price
                totalItem -= 1
                holder.subtotal.text = subtotal.toString()
                holder.adder.text = totalItem.toString()
            }
        }
        Glide.with(holder.itemView)
            .load(character.image)
            .thumbnail(0.1f)
            .into(holder.image)
    }

    inner class ViewHolder(binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.characterImage
        val name : TextView = binding.characterName
        val price : TextView = binding.characterPrice
        val subtotal : TextView = binding.subtotalItem
        val adder : TextView = binding.characterAdder
        val add : ImageButton = binding.btnAdd
        val remove : ImageButton = binding.btnRemove
    }

    fun updateCharacters(characters: List<Character>){
        this.characters = characters
        notifyDataSetChanged()
    }

}