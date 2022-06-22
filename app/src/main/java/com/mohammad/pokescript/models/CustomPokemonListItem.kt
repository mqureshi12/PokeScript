package com.mohammad.pokescript.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// To be shown in pokemon fragment

// Made because response from api doesn't contain images
// I will first fetch the list and fetch the details for the pokemon,
// and then map that to the custom pokemon list item with the image and type
// Easier and less resource intensive in my opinion than sending the whole pokemon
// detail item throughout the app and the list views

// Make pokemon name unique to stop duplicate items
// If I get pokemon with the same name, I'll replace it with the new value
@Parcelize
@Entity(
    tableName = "pokemon", indices = (arrayOf(Index(value = arrayOf("name"), unique = true)))
)

data class CustomPokemonListItem(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null, // ID for pokemon in room db

    @ColumnInfo(name = "api")
    val apiId: Int, // Used to query api

    @ColumnInfo(name = "image")
    val Image: String? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String, // Used for filtering more can be added later

    // Below will be used to plot pokemon on the map in map fragment

    @ColumnInfo(name = "positionLeft")
    val positionLeft: Int? = null,

    @ColumnInfo(name = "positionTop")
    val positionTop: Int? = null,

    @ColumnInfo(name = "isSaved")
    var isSaved: String = "false"

) : Parcelable