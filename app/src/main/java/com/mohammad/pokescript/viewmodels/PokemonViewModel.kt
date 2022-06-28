package com.mohammad.pokescript.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.repository.MainRepository
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    // Need to initialize live data
    // One live data object for pokemon list
    // Post the list from the api to live data
    // UI will then observe live data and react

    private val _pokemonList = SingleLiveEvent<Resource<List<CustomPokemonListItem>>>()
    val pokemonList: LiveData<Resource<List<CustomPokemonListItem>>>
        get() = _pokemonList

    // Runs as soon as the view model is created
    init {
        getPokemonList()
    }

    fun getPokemonList() {
        _pokemonList.postValue(Resource.Loading("Loading"))
        // All the work called in this block will be bound to the
        // lifecycle of the view model
        // Once the view model dies, the work and process with it
        // will also die
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonList())
        }
    }

    fun getNextPage(){
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonListNext())
        }
    }
}