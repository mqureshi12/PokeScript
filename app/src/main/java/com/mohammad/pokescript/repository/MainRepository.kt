package com.mohammad.pokescript.repository

import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem
import com.mohammad.pokescript.utilities.Resource

// To detail functions that will be needed to create an instance of my repository class

interface MainRepository {

    suspend fun getPokemonList() : Resource<List<CustomPokemonListItem>>
    suspend fun getPokemonListNext() : Resource<List<CustomPokemonListItem>>
    suspend fun getPokemonDetails(id: Int) : Resource<PokemonDetailItem>
    suspend fun getLastStored() : CustomPokemonListItem
    suspend fun getSavedPokemon() : Resource<List<CustomPokemonListItem>>
    // For saving pokemon in the detail view
    suspend fun savePokemon(pokemonListItem: CustomPokemonListItem)
}