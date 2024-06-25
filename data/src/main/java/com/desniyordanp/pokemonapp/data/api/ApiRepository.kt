package com.desniyordanp.pokemonapp.data.api

import com.denisyordanp.pokemonapp.schema.response.PokemonListResponse

interface ApiRepository {
    suspend fun fetchPokemonList(offset: Int): PokemonListResponse
}