package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.domain.api.LoadMyPokemon
import com.denisyordanp.pokemonapp.domain.mapper.toPokemon
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadMyPokemonImpl @Inject constructor(
    private val repository: MyPokemonRepository
) : LoadMyPokemon {
    override fun invoke() = repository.getMyPokemons().map { pokemons ->
        pokemons.map { it.toPokemon() }
    }
}