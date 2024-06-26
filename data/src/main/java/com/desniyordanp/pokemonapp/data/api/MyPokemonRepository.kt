package com.desniyordanp.pokemonapp.data.api

import com.denisyordanp.pokemonapp.schema.entity.MyPokemonEntity
import kotlinx.coroutines.flow.Flow

interface MyPokemonRepository {
    fun getMyPokemons(): Flow<List<MyPokemonEntity>>

    suspend fun getMyPokemon(id: String): MyPokemonEntity?

    suspend fun addMyPokemon(pokemon: MyPokemonEntity)
    suspend fun removeMyPokemon(id: String)
}