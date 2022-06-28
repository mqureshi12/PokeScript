package com.mohammad.pokescript.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mohammad.pokescript.FilterDialog
import com.mohammad.pokescript.R
import com.mohammad.pokescript.adapters.PokemonListAdapter
import com.mohammad.pokescript.databinding.FragmentPokemonBinding
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.viewmodels.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonFragment : Fragment(R.layout.fragment_pokemon), FilterDialog.FilterListener {

    private var shouldPaginate = true
    private lateinit var pokemonListAdapter: PokemonListAdapter
    private var pokemonList = mutableListOf<CustomPokemonListItem>()
    private lateinit var binding: FragmentPokemonBinding
    private val viewModel: PokemonViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokemonBinding.bind(view)
        setupRV()
        setupClicks()
        setupSearchView()
        setupFABs()
        initObserver()
    }

    private fun setupFABs() {
        binding.pokemonFragmentMapFAB.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_mapViewFragment)
        }
        binding.pokemonFragmentSavedFAB.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_savedViewFragment)
        }
    }

    private fun setupRV() {
        pokemonListAdapter = PokemonListAdapter()
        
        pokemonListAdapter.setOnClickListener(object : PokemonListAdapter.OnClickListener {
            override fun onClick(item: CustomPokemonListItem) {
                val bundle = Bundle()
                bundle.putParcelable("pokemon", item)
                findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
            }
        })
        binding.pokemonFragmentRV.apply { 
            adapter = pokemonListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(!recyclerView.canScrollVertically(1) && binding.pokemonFragmentSearch.query.isEmpty()) {
                        binding.pokemonFragmentPaginateProgress.visibility = View.VISIBLE
                        viewModel.getNextPage()
                    }
                }
            })
        }
        binding.pokemonFragmentRefresh.setOnRefreshListener {
            if(binding.pokemonFragmentSearch.query.isEmpty()) {
                viewModel.getPokemonList()
            } else {
                binding.pokemonFragmentRefresh.isRefreshing = false
            }
        }
    }

    private fun setupClicks() {
        binding.pokemonFragmentFilter.setOnClickListener {
            val dialog = FilterDialog(this)
            val transaction = childFragmentManager.beginTransaction()
            transaction.add(dialog, "Filter-dialog")
            transaction.commit()
        }
    }

    private fun setupSearchView() {
        binding.pokemonFragmentSearch.setOnClickListener {
            if(binding.pokemonFragmentSearch.isEmpty()) {
                pokemonListAdapter.submitList(mutableListOf())
                viewModel.getPokemonList()
            }
        }
        binding.pokemonFragmentSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) {
                    // Set the list
                    pokemonListAdapter.submitList(filterListByName(query))
                } else {
                    // Set the list to empty
                    pokemonListAdapter.submitList(mutableListOf())
                    viewModel.getPokemonList()
                }
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null) {
                    pokemonListAdapter.submitList(filterListByName(query))
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPokemonList()
    }

    private fun initObserver() {
        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { list ->
            when (list) {
                is Resource.Success -> {
                    if (list.data != null) {
                        if (list.data.isNotEmpty()) {
                            pokemonList = list.data as ArrayList<CustomPokemonListItem>
                            pokemonListAdapter.updatingList(list.data)
                            pokemonListAdapter.notifyDataSetChanged()
                            showProgressBar(false)
                            // Swipe to refresh layout is used then lets stop the refresh animation here
                            if (binding.pokemonFragmentRefresh.isRefreshing) {
                                binding.pokemonFragmentRefresh.isRefreshing = false
                            }
                        } else {
                            // Setup empty RV
                            showProgressBar(false)
                            showEmptyRecyclerViewError()
                        }
                    } else {
                        showEmptyRecyclerViewError()
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    // Setup empty RV
                    showEmptyRecyclerViewError()

                }
                is Resource.Loading -> {
                    showProgressBar(true)
                }
            }
        })
    }

    private fun showEmptyRecyclerViewError() {
        Toast.makeText(requireContext(), "No items found", Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.pokemonFragmentProgress.isVisible = isVisible
        binding.pokemonFragmentPaginateProgress.visibility = View.GONE
    }

    override fun typeToSearch(type: String) {
        // Don't want to paginate the list of results
        shouldPaginate = false
        pokemonListAdapter.submitList(filterListByType(type))
    }

    private fun filterListByType(type: String) : List<CustomPokemonListItem> {
        return pokemonList.filter { it.type == type }
    }

    private fun filterListByName(name: String) : List<CustomPokemonListItem> {
        return pokemonList.filter{it.name.contains(name)}
    }
}