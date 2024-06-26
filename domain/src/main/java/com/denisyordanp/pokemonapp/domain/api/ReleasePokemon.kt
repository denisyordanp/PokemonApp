package com.denisyordanp.pokemonapp.domain.api

interface ReleasePokemon {
    suspend operator fun invoke(id: String)
}