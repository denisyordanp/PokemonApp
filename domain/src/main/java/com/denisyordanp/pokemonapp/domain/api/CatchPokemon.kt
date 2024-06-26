package com.denisyordanp.pokemonapp.domain.api

import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail

interface CatchPokemon {
    suspend operator fun invoke(pokemon: PokemonDetail, currentTime: Long, nickname: String)
}