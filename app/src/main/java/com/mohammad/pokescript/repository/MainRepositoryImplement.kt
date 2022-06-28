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
            for (i in 1..10){
                // i is ID
                when (val apiResult = getPokemonDetails(i)) {
                    is Resource.Success -> {
                        if (apiResult.data != null) {
                            // Cast as new pokemon
                            apiResult.data.let { newPokemon ->
                                // Create custom pokemon object save in DB
                                val newPokemonObj = CustomPokemonListItem(
                                    name = newPokemon.name,
                                    Image = newPokemon.sprites.front_default,
                                    type = newPokemon.types?.get(0)?.type?.name.toString(),
                                    // Set positions for map
                                    positionLeft = (0..1500).random(), // Random position of pokemon
                                    positionTop = (0..1500).random(), // Random position of pokemon
                                    apiId = newPokemon.id
                                )
                                preSeedList.add(newPokemonObj)
                            }
                        } else {
                            return Resource.Error("Unable to retrieve next items")
                        }
                    }
                    else -> return Resource.Error("Unable to retrieve next items")
                }
            }
            // Insert into DB
            pokeDB.insertPokemonList(preSeedList)
            // Read from DB
            val initialDBRead = pokeDB.getPokemon()
            // Return from DB
            return Resource.Success(initialDBRead)
        }
    }

    override suspend fun getPokemonListNext(): Resource<List<CustomPokemonListItem>> {
        // Get id of last pokemon in local DB
        val lastStored = getLastStored()
        // Check API for details on next pokemon
        val nextPokemonID = lastStored.apiId + 1
        val pokemonList = mutableListOf<CustomPokemonListItem>()

        // Get next 10 pokemon
        for (i in nextPokemonID..(nextPokemonID + 9)){
            when (val apiResult = getPokemonDetails(i)) {
                is Resource.Success -> {
                    if (apiResult.data != null) {
                        apiResult.data.let { newPokemon ->
                            // Create custom pokemon object save in DB
                            val newPokemonObj = CustomPokemonListItem(
                                name = newPokemon.name,
                                Image = newPokemon.sprites.front_default,
                                type = newPokemon.types?.get(0)?.type?.name.toString(),
                                // Set positions for map
                                positionLeft = (0..1500).random(), // Random position of pokemon
                                positionTop = (0..1500).random(), // Random position of pokemon
                                apiId = newPokemon.id
                            )
                            pokemonList.add(newPokemonObj)

                        }
                    } else {
                        return Resource.Error("Unable to retrieve next items")
                    }
                }
                else -> return Resource.Error("Unable to retrieve next items")
            }
        }

        // Insert list into DB
        if (pokemonList.isNotEmpty()){
            pokeDB.insertPokemonList(pokemonList)
        }
        return Resource.Success(pokeDB.getPokemon())
    }

    override suspend fun getPokemonDetails(id: Int): Resource<PokemonDetailItem> {
        // First check DB for results
        val dbResult = pokeDB.getPokemonDetails(id)
        // Check if I have that pokemon in the DB
        if(dbResult != null) {
            // Is cache is valid, I'll return from the db
            // If not, I'll fetch from the api
            return if(dbResult.timestamp?.toLong()!! < fiveAgo) {
                getPokemonDetailsFromApi(id, dbResult)
            } else {
                Resource.Success(dbResult)
            }
        } else {
            return getPokemonDetailsFromApi(id, dbResult)
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
    private suspend fun getPokemonDetailsFromApi(id: Int, dbResult: PokemonDetailItem? ): Resource<PokemonDetailItem>{
        try {
            val apiResult = pokeApi.getPokemonDetails(id)
            if (apiResult.isSuccessful) {
                if (apiResult.body() != null) {
                    // Add timestamp
                    val newPokemon = apiResult.body()
                    newPokemon!!.timestamp = System.currentTimeMillis().toString()
                    // Store results in DB
                    pokeDB.insertPokemonDetailsItem(newPokemon)
                    // Retrieve results from DB
                    val newDBRead = pokeDB.getPokemonDetails(id)
                    // Return from DB
                    return Resource.Success(newDBRead!!)
                } else if (dbResult != null) {
                    // Return expired object to let user know cache has expired and we cannot find new items from Api
                    return Resource.Expired("Cache expired and cannot retrieve new Pokemon", dbResult)
                }
            } else {
                return Resource.Error(apiResult.message())
            }
        } catch (e: Exception) {
            return Resource.Error("Error retrieving results")
        }
        return Resource.Error("Error retrieving results")
    }
}