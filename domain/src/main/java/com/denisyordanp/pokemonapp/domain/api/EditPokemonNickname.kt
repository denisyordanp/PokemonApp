package com.denisyordanp.pokemonapp.domain.api

import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail

interface EditPokemonNickname {
    suspend operator fun invoke(pokemon: PokemonDetail, currentTIme: Long)
}