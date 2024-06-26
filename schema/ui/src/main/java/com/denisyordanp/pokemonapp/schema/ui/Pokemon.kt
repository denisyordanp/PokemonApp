package com.denisyordanp.pokemonapp.schema.ui

data class Pokemon(
    val id: String,
    val name: String,
    val nickname: String? = null
)