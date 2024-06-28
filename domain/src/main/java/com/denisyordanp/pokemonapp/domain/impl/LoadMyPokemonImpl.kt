package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.LoadMyPokemon
import com.denisyordanp.pokemonapp.domain.mapper.toPokemon
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadMyPokemonImpl @Inject constructor(
    private val repository: MyPokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LoadMyPokemon {
    override fun invoke() = repository.getMyPokemons().map { pokemons ->
        pokemons.map { it.toPokemon() }
    }.flowOn(dispatcher)
}