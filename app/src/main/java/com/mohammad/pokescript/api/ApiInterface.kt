package com.mohammad.pokescript.api

import com.mohammad.pokescript.models.PokemonDetailItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("/api/v2/pokemon/{id}")
    // Can be paused and resumed at a later time
    suspend fun getPokemonDetails(@Path("id") id: Int) : Response<PokemonDetailItem>
}