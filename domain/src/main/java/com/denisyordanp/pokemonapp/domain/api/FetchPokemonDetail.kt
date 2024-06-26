package com.denisyordanp.pokemonapp.domain.api

import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail

interface FetchPokemonDetail {
    suspend operator fun invoke(id: String): PokemonDetail
}