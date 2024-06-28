package com.desniyordanp.pokemonapp.data.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.core.database.dao.MyPokemonDao
import com.denisyordanp.pokemonapp.schema.entity.MyPokemonEntity
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyPokemonRepositoryImpl @Inject constructor(
    private val dao: MyPokemonDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : MyPokemonRepository {
    override fun getMyPokemons() = dao.getMyPokemons().flowOn(dispatcher)
    override suspend fun getMyPokemon(id: String) = withContext(dispatcher) {
        dao.getMyPokemonById(id)
    }
    override suspend fun editPokemonNickname(pokemon: MyPokemonEntity) = withContext(dispatcher) {
        dao.upsertPokemon(pokemon)
    }

    override suspend fun addMyPokemon(pokemon: MyPokemonEntity) = withContext(dispatcher) {
        dao.upsertPokemon(pokemon)
    }

    override suspend fun removeMyPokemon(id: String) = withContext(dispatcher) {
        dao.remove(id)
    }
}