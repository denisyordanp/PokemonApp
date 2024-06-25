package com.denisyordanp.pokemonapp.domain.api

import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon

interface FetchPokemons {
    suspend operator fun invoke(offset: Int? = null): Paging<Pokemon>
}