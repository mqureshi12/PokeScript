package com.mohammad.pokescript.api

import retrofit2.Response
import retrofit2.http.Path

interface ApiInterface {

    // Can be paused and resumed at a later time
    suspend fun getPokemonDetails(@Path("id") id: Int) : Response<PokemonDetailItem>
}