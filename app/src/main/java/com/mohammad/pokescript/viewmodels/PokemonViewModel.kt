package com.mohammad.pokescript.viewmodels

import com.mohammad.pokescript.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val repository: MainRepository) {

    // Need to initialize live data
    // One live data object for pokemon list
    // Post the list from the api to live data
    // UI will then observe live data and react


}