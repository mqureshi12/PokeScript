package com.mohammad.pokescript.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohammad.pokescript.databinding.PokemonSavedItemBinding
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.utilities.Image
import java.util.*

class SavedListAdapter() : RecyclerView.Adapter<SavedListAdapter.PokemonViewHolder>() {

    private var onClickListener : OnClickListener? = null
    private var onDeleteListener: OnDeleteListener? = null
    private var pokemonList = mutableListOf<CustomPokemonListItem>()

    class PokemonViewHolder(private val binding: PokemonSavedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CustomPokemonListItem, onClickListener: OnClickListener?, onDeleteListener: OnDeleteListener?, position: Int) {
            binding.rowCardTitle.text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                Locale.ROOT) else it.toString() }

            binding.rowCardType.text = "Type: ${item.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                Locale.ROOT) else it.toString() }}"

            binding.cardView.setOnClickListener {
                onClickListener?.onClick(item)
            }

            // Set up image
            item.Image?.let { Image.loadImage(binding.rowCardIV, it)}

            binding.pokemonDelete.setOnClickListener {
                onDeleteListener?.onDelete(item, position)
            }
        }

        companion object {
            fun inflateLayout(parent: ViewGroup): PokemonViewHolder {
                parent.apply {
                    val inflater = LayoutInflater.from(parent.context)
                    val binding = PokemonSavedItemBinding.inflate(inflater, parent, false)
                    return PokemonViewHolder(binding)
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(item: CustomPokemonListItem)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnDeleteListener {
        fun onDelete(item: CustomPokemonListItem, position: Int)
    }

    fun setOnDeleteListener(onDeleteListener: OnDeleteListener) {
        this.onDeleteListener = onDeleteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.inflateLayout(parent)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position], onClickListener, onDeleteListener, position)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    // Delete pokemon
    fun removeItemAtPosition(position: Int) {
        pokemonList.removeAt(position)
    }

    fun setList(list: List<CustomPokemonListItem>) {
        pokemonList.clear()
        pokemonList = list as MutableList<CustomPokemonListItem>
    }
}