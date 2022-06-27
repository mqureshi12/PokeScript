package com.mohammad.pokescript.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mohammad.pokescript.R
import com.mohammad.pokescript.databinding.FragmentDetailBinding
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem
import com.mohammad.pokescript.utilities.Image
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var pokemon: CustomPokemonListItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        // Check if pokemon was passed in as an argument
        arguments?.let {
            it.getParcelable<CustomPokemonListItem>("pokemon")?.let { mPokemon ->
                pokemon = mPokemon
                pokemon.type?.let { type -> setType(type) }
                binding.detailFragmentTitleName.text = pokemon.name.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(
                        Locale.getDefault()
                    ) else char.toString()
                }

                getPokemonDetails(pokemon.apiId)
                initObserver()
            }

            if(this::pokemon.isInitialized) {
                // Check if saved
                if(pokemon.isSaved == "false") {
                    binding.detailFragmentSaveBTN.setOnClickListener {
                        pokemon.isSaved = "true"
                        viewModel.savePokemon(pokemon)
                        Toast.makeText(requireContext(), "Pokemon has been saved to your party", Toast.LENGTH_SHORT).show()
                        binding.detailFragmentSaveBTN.text = "Saved"
                    }
                } else {
                    binding.detailFragmentSaveBTN.text = "Saved"
                }
            }
        }
    }

    private fun setType(type: String) {
        binding.detailFragmentType.text = "Type : ${type.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }}"
    }

    private fun getPokemonDetails(id: Int?) {
        if(id != null) {
            viewModel.getPokemonDetails(id)
        }
    }

    private fun initObserver() {
        // Observe pokemon list
        viewModel.pokemonDetails.observe(viewLifecycleOwner, androidx.lifecycle.Observer { details ->
            // Switch statement to switch my cases such as success or error
            when(details) {
                is Resource.Success -> {
                    details.data?.let {
                        setupView(it)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "No Pokemon details found", Toast.LENGTH_SHORT).show()
                }
                is Resource.Expired -> {
                    details.data?.let {
                        setupView(it)
                    }
                    Toast.makeText(requireContext(), "Unable to retrieve up to date info", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
            }
        })
    }

    private fun setupView(pokemonDetails: PokemonDetailItem) {
        pokemonDetails.sprites.otherSprites.artwork.front_default?.let {
            Image.loadImage(binding.detailFragmentIV, it)
        }

        for(i in pokemonDetails.abilities) {
            val textView = TextView(requireContext())
            textView.text = i.ability.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            textView.textSize = 15f
            textView.setTextColor(Color.WHITE)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            binding.abilitiesContainer.addView(textView)
        }

        val pokemonStats = mutableListOf<Int>()

        for(i in pokemonDetails.stat) {
            val textView = TextView(requireContext())
            val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal)
            progressBar.progress = i.baseStat ?: 0
            progressBar.progressTintList = ColorStateList.valueOf(Color.WHITE)
            textView.text = i.stat.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            textView.textSize = 15f
            textView.setTextColor(Color.WHITE)
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            i.baseStat?.let { pokemonStats.add(it) }
            binding.statsContainer.addView(textView)
            binding.statsContainer.addView(progressBar)
        }

        // Rating setup: 1 star for weak pokemon, 2 star for average pokemon, 3 star for strong pokemon
        val pokemonAvg = pokemonStats.sum() / 6
        val dp = (40 * (requireContext().resources.displayMetrics.density)).toInt()
        addStarToContainer(dp)
        if(pokemonAvg > 60) {
            addStarToContainer(dp)
        }
        if(pokemonAvg > 79) {
            addStarToContainer(dp)
        }

        pokemon.Image?.let {
            Image.loadImage(binding.mapViewPlot, it)
        }
        Image.setMargins(
            binding.mapViewPlot,
            viewModel.plotLeft,
            viewModel.plotTop
        )
    }

    private fun addStarToContainer(dp: Int) {
        val img = ImageView(requireContext())
        val lp = LinearLayout.LayoutParams(dp, dp)
        img.layoutParams = lp
        Image.loadImageDrawable(img, R.drawable.star)
        binding.detailFragmentStarContainer.addView(img)
    }
}