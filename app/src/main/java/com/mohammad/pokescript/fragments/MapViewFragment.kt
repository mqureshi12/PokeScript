package com.mohammad.pokescript.fragments

import android.os.Bundle
import android.util.Log
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

private const val TAG = "MapViewFragment"

@AndroidEntryPoint
class MapViewFragment : Fragment(R.layout.fragment_map) {

    /*
    Normally, properties declared as having a non-null type must be initialized in the constructor.
    However, it is often the case that doing so is not convenient. For example, properties can be initialized through dependency injection,
    or in the setup method of a unit test. In these cases, you cannot supply a non-null initializer in the constructor, but you still want
    to avoid null checks when referencing the property inside the body of a class.
    To handle such cases, you can mark the property with the lateinit modifier (kotlinlang.org).
    */
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
            when (list) {
                is Resource.Success -> {
                    if (list.data != null) {
                        if (list.data.isNotEmpty()) {
                            Log.d(TAG, list.data.toString())
                            showProgress(false)
                            for (i in list.data) {
                                Log.d(TAG, i.name)
                                val img = ImageView(requireContext())
                                val lp = RelativeLayout.LayoutParams(200, 200) //make the image a bit bigger in this fragment
                                img.layoutParams = lp
                                // Setup last location plot
                                i.Image?.let { it1 -> Image.loadImage(requireContext(), img, it1) }
                                // Setup random position
                                i.positionLeft?.let { left ->
                                    i.positionTop?.let { right ->
                                        Image.setMargins(img, left, right)
                                    }
                                }
                                // Add img to map
                                binding.mapFragmentImgLayout.addView(img)
                            }
                        } else {
                            // Setup empty recyclerview
                            showProgress(false)
                            Toast.makeText(requireContext(), "No Pokemon found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d(TAG, list.message.toString())
                    showProgress(false)
                    Toast.makeText(requireContext(), "No Pokemon found", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgress(true)
                }
            }
        })
    }
}