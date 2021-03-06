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
    private val pokeDao: PokemonDao

) : MainRepository {

    // Cache
    private val fiveAgo = System.currentTimeMillis() - Constants.CACHE

    override suspend fun getPokemonList(): Resource<List<CustomPokemonListItem>> {
        // Check for results from DB
        val responseFromDB = pokeDao.getPokemon()
        if (responseFromDB.isNotEmpty()) {
            return Resource.Success(responseFromDB)
        } else {
            // If return null then preSeed from Constants
            val preSeedList = Constants.preSeedDB()
            // Insert into DB
            pokeDao.insertPokemonList(preSeedList)
            // Read from DB
            val initialDBRead = pokeDao.getPokemon()
            // Return from DB
            return Resource.Success(initialDBRead)

        }
    }

    override suspend fun getPokemonListNext(): Resource<List<CustomPokemonListItem>> {
        // Get id of last pokemon in local DB
        val lastStoredPokemonObject = getLastStored()
        // Check API for details on next pokemon
        val nextPokemonID = lastStoredPokemonObject.apiId + 1
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
            pokeDao.insertPokemonList(pokemonList)
        }

        return Resource.Success(pokemonList)
    }

    override suspend fun getPokemonDetails(id: Int): Resource<PokemonDetailItem> {
        // First check DB for results
        val dbResult = pokeDao.getPokemonDetails(id)
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
        return pokeDao.getLastStoredPokemonObject()
    }

    override suspend fun getSavedPokemon(): Resource<List<CustomPokemonListItem>> {
        val dbResult = pokeDao.getSavedPokemon()
        return if (dbResult.isNullOrEmpty()) {
            Resource.Error("Saved pokemon list is empty")
        } else {
            Resource.Success(dbResult)
        }
    }

    override suspend fun savePokemon(pokemonListItem: CustomPokemonListItem) {
        pokeDao.insertPokemon(pokemonListItem)
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
                    pokeDao.insertPokemonDetailsItem(newPokemon)
                    // Retrieve results from DB
                    val newDBRead = pokeDao.getPokemonDetails(id)
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