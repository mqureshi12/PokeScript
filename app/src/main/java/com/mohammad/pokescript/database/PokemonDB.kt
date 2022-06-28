package com.mohammad.pokescript.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohammad.pokescript.models.Converters
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem

@Database(entities = [CustomPokemonListItem::class, PokemonDetailItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokemonDB : RoomDatabase() {

    // Function for access DAO
    abstract fun pokemonDAO(): PokemonDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: PokemonDB? = null

        fun getDB(context: Context): PokemonDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDB::class.java,
                    "Pokemon"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Return instance
                instance
            }
        }
    }
}