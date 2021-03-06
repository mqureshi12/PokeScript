package com.mohammad.pokescript.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem
import com.mohammad.pokescript.repository.MainRepository
import com.mohammad.pokescript.utilities.Resource
import com.mohammad.pokescript.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _pokemonDetails = SingleLiveEvent<Resource<PokemonDetailItem>>()
    val pokemonDetails: LiveData<Resource<PokemonDetailItem>>
        get() = _pokemonDetails

    private val _pokemonSaveIntent = MutableLiveData<Boolean>()
    val pokemonSaveIntent: LiveData<Boolean>
        get() = _pokemonSaveIntent

    // Values for plotting on the map
    // When saving the pokemon, add these values
    val plotLeft = (0..600).random()
    val plotTop = (0..600).random()

    fun getPokemonDetails(id: Int) {
        _pokemonDetails.postValue(Resource.Loading(""))
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetails.postValue(repository.getPokemonDetails(id))
        }
    }

    fun savePokemon(customPokemonListItem: CustomPokemonListItem) {
        _pokemonSaveIntent.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePokemon(customPokemonListItem)
        }
    }
}