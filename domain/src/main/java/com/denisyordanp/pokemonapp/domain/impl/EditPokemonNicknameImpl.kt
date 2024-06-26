package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.EditPokemonNickname
import com.denisyordanp.pokemonapp.domain.mapper.toMyPokemonEntity
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditPokemonNicknameImpl @Inject constructor(
    private val repository: MyPokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : EditPokemonNickname {
    override suspend fun invoke(pokemon: PokemonDetail, currentTIme: Long) = withContext(dispatcher) {
        repository.editPokemonNickname(pokemon.toMyPokemonEntity(currentTIme))
    }
}