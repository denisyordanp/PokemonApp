package com.denisyordanp.pokemonapp.ui.screen.detail

import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail

sealed class PokemonDetailActionType {
    open val pokemon: PokemonDetail? = null
    data class Catch(override val pokemon: PokemonDetail) : PokemonDetailActionType()
    data class Release(override val pokemon: PokemonDetail) : PokemonDetailActionType()
    data class EditNickname(override val pokemon: PokemonDetail) : PokemonDetailActionType()

    data object Idle : PokemonDetailActionType()
}
