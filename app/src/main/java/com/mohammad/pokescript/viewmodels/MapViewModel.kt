package com.mohammad.pokescript.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.repository.MainRepository
import com.mohammad.pokescript.utilities.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _pokemonList = MutableLiveData<Resource<List<CustomPokemonListItem>>>()
    // Getter. Value accessible from the ui
    val pokemonList: LiveData<Resource<List<CustomPokemonListItem>>>
        get() = _pokemonList

    fun getPokemonList() {
        _pokemonList.postValue(Resource.Loading(""))
        // All the work called in this block will be bound to the
        // lifecycle of the view model
        // Once the view model dies, the work and process with it
        // will also die
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonList())
        }
    }
}