package com.denisyordanp.pokemonapp.schema.ui

data class Paging<T>(
    val offset: Int,
    val data: List<T>
)
