package com.denisyordanp.pokemonapp.schema.ui


data class PokemonDetail(
    val baseExperience: Int,
    val height: Int,
    val id: Int,
    val name: String,
    val species: Species,
    val weight: Int
)

data class Species(
    val name: String
)