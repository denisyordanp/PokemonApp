package com.desniyordanp.pokemonapp.data.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.core.network.ApiService
import com.denisyordanp.pokemonapp.schema.response.PokemonDetailResponse
import com.desniyordanp.pokemonapp.data.api.ApiRepository
import com.desniyordanp.pokemonapp.data.config.ServiceConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val service: ApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ApiRepository {
    override suspend fun fetchPokemonList(offset: Int) = withContext(dispatcher) {
        service.fetchPokemon(offset = offset, limit = ServiceConfig.PAGING_ITEM_LIMIT)
    }

    override suspend fun fetchPokemonDetail(id: String) = withContext(dispatcher) {
        service.fetchUserDetail(id)
    }
}