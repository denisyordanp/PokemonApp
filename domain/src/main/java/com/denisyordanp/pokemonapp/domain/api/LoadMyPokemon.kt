package com.denisyordanp.pokemonapp.domain.api

import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import kotlinx.coroutines.flow.Flow

interface LoadMyPokemon {
    operator fun invoke(): Flow<List<Pokemon>>
}