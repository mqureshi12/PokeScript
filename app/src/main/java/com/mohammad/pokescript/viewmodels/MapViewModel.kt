package com.mohammad.pokescript.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.repository.MainRepository
import com.mohammad.pokescript.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _pokemonList = MutableLiveData<Resource<List<CustomPokemonListItem>>>()
    val pokemonList: LiveData<Resource<List<CustomPokemonListItem>>>
        get() = _pokemonList

    fun getPokemonList() {
        _pokemonList.postValue(Resource.Loading(""))
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.postValue(repository.getPokemonList())
        }
    }
}