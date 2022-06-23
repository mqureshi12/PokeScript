package com.mohammad.pokescript.repository

import com.mohammad.pokescript.api.ApiInterface
import com.mohammad.pokescript.database.PokemonDao
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem
import com.mohammad.pokescript.utilities.Constants
import com.mohammad.pokescript.utilities.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepositoryImplement @Inject constructor (

    // Access to api and db
    private val pokeApi: ApiInterface,
    private val pokeDB: PokemonDao

) : MainRepository {

    // Cache
    private val fiveAgo = System.currentTimeMillis() - Constants.CACHE

    override suspend fun getPokemonList(): Resource<List<CustomPokemonListItem>> {
        val responseFromDB = pokeDB.getPokemon()

        if(responseFromDB.isNotEmpty()) {
            return Resource.Success(responseFromDB)
        } else {
            // Otherwise fetch the first ten pokemon and add to the list
            val preSeedList = mutableListOf<CustomPokemonListItem>()
            for(i in 1..10) {
                // i is ID
                when(val apiResult = getPokemonDetails(i)) {
                    is Resource.Success -> {
                        // Cast as new pokemon
                        apiResult.data?.let { newPokemon ->
                            val newPokemonObj = CustomPokemonListItem(
                                name = newPokemon.name,
                                Image = newPokemon.sprites.front_default,
                                type = newPokemon.types?.get(0)?.type?.name.toString(),
                                positionLeft = (0..1500).random(), // Random position of pokemon
                                positionTop = (0..1500).random(), // Random position of pokemon
                                apiId = newPokemon.id
                            )
                            preSeedList.add(newPokemonObj)
                        }
                    }
                    else -> {return Resource.Error("Unable to retrieve items")}
                }
            }

            // Insert list to DB
            pokeDB.insertPokemonList(preSeedList)
            return Resource.Success(pokeDB.getPokemon())
        }
    }

    override suspend fun getPokemonListNext(): Resource<List<CustomPokemonListItem>> {
        val lastStored = getLastStored()
        val nextPokemonID = lastStored.apiId + 1
        val pokemonList = mutableListOf<CustomPokemonListItem>()

        // Get next 10 pokemon
        for(i in nextPokemonID..(nextPokemonID + 9)) {
            // i is ID
            when(val apiResult = getPokemonDetails(i)) {
                is Resource.Success -> {
                    // Cast as new pokemon
                    apiResult.data?.let { newPokemon ->
                        val newPokemonObj = CustomPokemonListItem(
                            name = newPokemon.name,
                            Image = newPokemon.sprites.front_default,
                            type = newPokemon.types?.get(0)?.type?.name.toString(),
                            positionLeft = (0..1500).random(), // Random position of pokemon
                            positionTop = (0..1500).random(), // Random position of pokemon
                            apiId = newPokemon.id
                        )
                        pokemonList.add(newPokemonObj)
                    }
                }
                else -> {return Resource.Error("Unable to retrieve items")}
            }
        }

        // Insert list to DB
        pokeDB.insertPokemonList(pokemonList)
        return Resource.Success(pokeDB.getPokemon())
    }

    override suspend fun getPokemonDetails(id: Int): Resource<PokemonDetailItem> {
        val dbResult = pokeDB.getPokemonDetails(id)
        // Check if I have that pokemon in the DB
        if(dbResult != null) {
            // Is cache is valid, I'll return from the db
            // If not, I'll fetch from the api
            return if(dbResult.timestamp?.toLong()!! < fiveAgo) {
                getPokemonDetailsFromApi(id)
            } else {
                Resource.Success(dbResult)
            }
        } else {
            return getPokemonDetailsFromApi(id)
        }
    }

    override suspend fun getLastStored(): CustomPokemonListItem {
        return pokeDB.getLastStoredPokemonObject()
    }

    override suspend fun getSavedPokemon(): Resource<List<CustomPokemonListItem>> {
        val dbResult = pokeDB.getSavedPokemon()
        return if (dbResult.isNullOrEmpty()){
            Resource.Error("Saved pokemon list is empty")
        } else {
            Resource.Success(dbResult)
        }
    }

    override suspend fun savePokemon(pokemonListItem: CustomPokemonListItem) {
        pokeDB.insertPokemon(pokemonListItem)
    }

    // Suspend because I'm accessing the api
    private suspend fun getPokemonDetailsFromApi(id: Int) : Resource<PokemonDetailItem> {
        try {
            val apiResult = pokeApi.getPokemonDetails(id)
            if(apiResult.isSuccessful && apiResult.body() != null) {
                // Add in timestamp of retrieval which I'll use to check the cache later
                val newPokemon = apiResult.body()
                newPokemon!!.timestamp = System.currentTimeMillis().toString()
                pokeDB.insertPokemonDetailsItem(newPokemon)
                return Resource.Success(pokeDB.getPokemonDetails(id)!!)
            } else {
                return Resource.Error(apiResult.message())
            }
        } catch (e: Exception) {
            return Resource.Error("Error retrieving items")
        }
    }
}