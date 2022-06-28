package com.mohammad.pokescript.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohammad.pokescript.R
import com.mohammad.pokescript.databinding.FragmentMapBinding
import com.mohammad.pokescript.utilities.Image
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.viewmodels.MapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapViewFragment : Fragment(R.layout.fragment_map) {

    lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize binding
        binding = FragmentMapBinding.bind(view)
        // Back button image
        binding.mapFragmentBack.setOnClickListener {
            // Navigates back to previous fragment
            findNavController().popBackStack()
        }
        // Observes my live data instances from my view model
        initObserver()
        // When this fragment is created, launch a coroutine here to fetch the pokemon list
        lifecycleScope.launchWhenStarted {
            viewModel.getPokemonList()
        }
    }

    // Show and hide progress bar
    private fun showProgress(isVisible: Boolean) {
        binding.mapFragmentProgress.isVisible = isVisible
    }

    private fun initObserver() {
        // Observe pokemon list
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            // Switch statement to switch my cases such as success or error
            when(list) {
                is Resource.Success -> {
                    showProgress(false)
                    // Check if list is empty or null
                    if(list.data?.isNotEmpty() == true) {
                        list.data.forEach { pokemon ->
                            // For each pokemon in the list, make an iv
                            val img = ImageView(requireContext())
                            val lp = RelativeLayout.LayoutParams(200, 200)
                            img.layoutParams = lp
                            pokemon.Image?.let { Image.loadImage(requireContext(), img, it) }
                            // Positions of the image
                            pokemon.positionLeft?.let { left ->
                                pokemon.positionTop?.let { top ->
                                    Image.setMargins(img, left, top)
                                }
                            }
                            binding.mapFragmentImgLayout.addView(img)
                        }
                    }
                }
                is Resource.Error -> {
                    showProgress(false)
                    Toast.makeText(requireContext(), "No Pokemon found", Toast.LENGTH_SHORT).show()
                }
                is Resource.Expired -> {}
                is Resource.Loading -> {
                    showProgress(true)
                }
            }
        })
    }
}