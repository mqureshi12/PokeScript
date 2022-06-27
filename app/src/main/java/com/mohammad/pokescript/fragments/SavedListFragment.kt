package com.mohammad.pokescript.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohammad.pokescript.R
import com.mohammad.pokescript.adapters.SavedListAdapter
import com.mohammad.pokescript.databinding.FragmentSavedBinding
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.viewmodels.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedListFragment : Fragment(R.layout.fragment_saved) {

    private lateinit var pokemonSavedListAdapter: SavedListAdapter
    private var count = 0
    private var savedList = mutableListOf<CustomPokemonListItem>()
    private lateinit var binding: FragmentSavedBinding
    private val viewModel: SavedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedBinding.bind(view)
        binding.savedFragmentBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.pokemonFragmentSettings.setOnClickListener {
            deleteAllPokemon()
        }

        lifecycleScope.launchWhenStarted {
            setupRV()
            initObserver()
            viewModel.getSavedPokemon()
        }
    }

    private fun deletePokemon(customPokemonListItem: CustomPokemonListItem, pos: Int) {
        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
        builder.setMessage("Are you sure you want to delete this Pokemon?")
            .setCancelable(false)
            .setPositiveButton("Yes"){dialog, id ->
                customPokemonListItem.isSaved = "false"
                pokemonSavedListAdapter.removeItemAtPosition(pos)
                pokemonSavedListAdapter.notifyDataSetChanged()
                count -= 1
                if(count == 0) {
                    binding.savedFragmentPlaceholder.isVisible = true
                }
                viewModel.deletePokemon(customPokemonListItem)
            }
            .setNegativeButton("No"){dialog, id ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteAllPokemon() {
        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
        builder.setMessage("Are you sure you want to delete ALL Pokemon?")
            .setCancelable(false)
            .setPositiveButton("Yes"){dialog, id ->
                if(count > 0) {
                    savedList.forEach {
                        it.isSaved = "false"
                        viewModel.deletePokemon(it)
                    }
                }
                savedList.clear()
                pokemonSavedListAdapter.setList(savedList)
                pokemonSavedListAdapter.notifyDataSetChanged()
                binding.savedFragmentPlaceholder.isVisible = true
            }
            .setNegativeButton("No"){dialog, id ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setupRV() {
        pokemonSavedListAdapter = SavedListAdapter()

        pokemonSavedListAdapter.setOnClickListener(object : SavedListAdapter.OnClickListener {
            override fun onClick(item: CustomPokemonListItem) {
                // Pass over to detail fragment to see list of all pokemon info
                val bundle = Bundle ()
                bundle.putParcelable("pokemon", item)
                findNavController().navigate(R.id.action_savedViewFragment_to_detailFragment, bundle)
            }
        })
        // Delete listener
        pokemonSavedListAdapter.setOnDeleteListener(object : SavedListAdapter.OnDeleteListener {
            override fun onDelete(item: CustomPokemonListItem, position: Int) {
                deletePokemon(item, position)
            }
        })
        binding.savedFragmentRV.adapter = pokemonSavedListAdapter
    }

    private fun initObserver() {
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            when(list) {
                is Resource.Success -> {
                    if(list.data?.isNotEmpty() == true) {
                        count = list.data.size
                        savedList = list.data as MutableList<CustomPokemonListItem>
                        pokemonSavedListAdapter.setList(list.data)
                        pokemonSavedListAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    binding.savedFragmentPlaceholder.isVisible = true
                }
                is Resource.Expired -> {}
                is Resource.Loading -> {}
            }
        })
    }
}