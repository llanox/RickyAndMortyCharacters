package co.gabriel.rickyandmorty.presentation.character_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.gabriel.rickyandmorty.data.model.Character
import co.gabriel.rickyandmorty.databinding.CharacterItemBinding
import com.bumptech.glide.Glide

class CharacterRecyclerViewAdapter(private var characters : List<Character> ) :
    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.name.text = character.name

        Glide.with(holder.itemView)
            .load(character.image)
            .thumbnail(0.1f)
            .into(holder.image)
    }


    inner class ViewHolder(binding: CharacterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.characterImage
        val name : TextView = binding.characterName
    }

    fun updateCharacters(characters: List<Character>){
        this.characters = characters
        notifyDataSetChanged()
    }

}