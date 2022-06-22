package com.mohammad.pokescript.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem

// Dao class to provide methods to perform options on room db

@Dao
interface PokemonDao {

    // Pokemon table functions

    // Searches db and returns result if a name contains the string provided from the user
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :name || '%'")
    suspend fun searchPokemonByName(name: String): List<CustomPokemonListItem>?

    // Returns exact type matches from db
    @Query("SELECT * FROM pokemon WHERE type Like :type")
    suspend fun searchPokemonByType(type: String): List<CustomPokemonListItem>?

    @Query("SELECT * FROM pokemon")
    fun getPokemon(): List<CustomPokemonListItem>

    @Query("SELECT * FROM pokemon WHERE isSaved = 'true'")
    suspend fun getSavedPokemon(): List<CustomPokemonListItem>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPokemonList(list: List<CustomPokemonListItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(item: CustomPokemonListItem)

    // PokemonDetail table functions

    @Query("SELECT * FROM pokemonDetails WHERE id Like :id")
    suspend fun getPokemonDetails(id: Int): PokemonDetailItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonDetailsItem(pokemonDetailItem: PokemonDetailItem)

    // For cache. Last pokemon in this order is the last pokemon fetched
    // Can use this to find out whether cache has expired or not
    @Query("SELECT * FROM pokemon ORDER BY id DESC LIMIT 1")
    fun getLastStoredPokemonObject(): CustomPokemonListItem
}