package com.mohammad.pokescript.models

import com.google.gson.annotations.SerializedName

data class PokemonListItem(

    // Values I want to fetch from the api json body
    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String,

    @SerializedName("previous")
    val previous: String,

    @SerializedName("results")
    val results: List<PokemonResult>
)

data class PokemonResult(

    val name: String,
    val url: String
)
