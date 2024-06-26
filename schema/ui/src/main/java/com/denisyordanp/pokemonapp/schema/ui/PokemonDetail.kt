package com.denisyordanp.pokemonapp.schema.ui


data class PokemonDetail(
    val baseExperience: Int,
    val height: Int,
    val id: String,
    val name: String,
    val species: Species,
    val weight: Int,
    val nickname: String? = null,
    val isMyPokemon: Boolean
)

data class Species(
    val name: String
)