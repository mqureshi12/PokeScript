package com.mohammad.pokescript.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohammad.pokescript.models.Converters
import com.mohammad.pokescript.models.CustomPokemonListItem
import com.mohammad.pokescript.models.PokemonDetailItem
import java.security.AccessControlContext

@Database(entities = [CustomPokemonListItem::class, PokemonDetailItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokemonDB : RoomDatabase() {

    // Function for access DAO
    abstract fun pokemonDAO(): PokemonDao

    companion object {

        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: PokemonDB? = null

        fun getDB(context: Context) : PokemonDB {
            // If INSTANCE is not full, return it. If null, create DB
            return INSTANCE ?: synchronized(this) {
                // Want this available for whole app
                val instance = Room.databaseBuilder(context.applicationContext, PokemonDB::class.java, "Pokemon")
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return
                instance
            }
        }
    }
}