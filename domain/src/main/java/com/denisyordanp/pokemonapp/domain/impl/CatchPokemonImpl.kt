package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.CatchPokemon
import com.denisyordanp.pokemonapp.domain.mapper.toMyPokemonEntity
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatchPokemonImpl @Inject constructor(
    private val repository: MyPokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CatchPokemon {
    override suspend fun invoke(pokemon: PokemonDetail, currentTime: Long, nickname: String) =
        withContext(dispatcher) {
            repository.addMyPokemon(pokemon.toMyPokemonEntity(currentTime, nickname))
        }
}