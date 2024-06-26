package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.FetchPokemonDetail
import com.denisyordanp.pokemonapp.domain.mapper.toPokemonDetail
import com.desniyordanp.pokemonapp.data.api.ApiRepository
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchPokemonDetailImpl @Inject constructor(
    private val repository: ApiRepository,
    private val myPokemonRepository: MyPokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FetchPokemonDetail {
    override suspend fun invoke(id: String) = withContext(dispatcher) {
        val response = repository.fetchPokemonDetail(id)
        val isMyPokemon = myPokemonRepository.getMyPokemon(id) != null

        response.toPokemonDetail(isMyPokemon)
    }
}