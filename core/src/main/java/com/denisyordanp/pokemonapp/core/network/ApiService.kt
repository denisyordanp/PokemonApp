package com.denisyordanp.pokemonapp.core.network

import com.denisyordanp.pokemonapp.schema.response.PokemonDetailResponse
import com.denisyordanp.pokemonapp.schema.response.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun fetchPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun fetchUserDetail(
        @Path("id") id: String,
    ): PokemonDetailResponse
}