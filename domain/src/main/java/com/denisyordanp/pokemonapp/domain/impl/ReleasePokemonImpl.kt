package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.ReleasePokemon
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReleasePokemonImpl @Inject constructor(
    private val repository: MyPokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ReleasePokemon {
    override suspend fun invoke(id: String) = withContext(dispatcher) {
        repository.removeMyPokemon(id)
    }
}