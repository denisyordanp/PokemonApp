package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.common.extension.orZero
import com.denisyordanp.pokemonapp.domain.api.FetchPokemons
import com.denisyordanp.pokemonapp.domain.mapper.toPokemonPaging
import com.desniyordanp.pokemonapp.data.api.ApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchPokemonsImpl @Inject constructor(
    private val repository: ApiRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FetchPokemons {
    override suspend fun invoke(offset: Int?) = withContext(dispatcher) {
        val response = repository.fetchPokemonList(offset.orZero())
        response.toPokemonPaging()
    }
}