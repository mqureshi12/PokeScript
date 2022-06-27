package com.mohammad.pokescript.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohammad.pokescript.databinding.PokemonItemBinding
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.utilities.Image
import java.util.*

class PokemonListAdapter() : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    private var onClickListner : OnClickListener? = null
    private var pokemonList = mutableListOf<CustomPokemonListItem>()

    class PokemonViewHolder(private val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CustomPokemonListItem, onClickListener: OnClickListener?) {
            binding.rowCardTitle.text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                Locale.ROOT) else it.toString() }

            binding.rowCardType.text = "Type: ${item.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                Locale.ROOT) else it.toString() }}"

            binding.cardView.setOnClickListener {
                onClickListener?.onClick(item)
            }

            // Set up image
            item.Image?.let { Image.loadImage(itemView.context, binding.rowCardIV, it) }        }

        companion object {
            fun inflateLayout(parent: ViewGroup): PokemonViewHolder {
                parent.apply {
                    val inflater = LayoutInflater.from(parent.context)
                    val binding = PokemonItemBinding.inflate(inflater, parent, false)
                    return PokemonViewHolder(binding)
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(item: CustomPokemonListItem)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListner = onClickListner
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonListAdapter.PokemonViewHolder {
        return PokemonViewHolder.inflateLayout(parent)
    }

    override fun onBindViewHolder(holder: PokemonListAdapter.PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position], onClickListner)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    // Pagination
    fun updatingList(list: List<CustomPokemonListItem>) {
        pokemonList.addAll(list)
    }

    fun submitList(list: List<CustomPokemonListItem>) {
        pokemonList = list as MutableList<CustomPokemonListItem>
        notifyDataSetChanged()
    }
}