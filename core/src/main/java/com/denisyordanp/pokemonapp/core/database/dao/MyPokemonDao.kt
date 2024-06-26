package com.denisyordanp.pokemonapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.denisyordanp.pokemonapp.schema.entity.MyPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MyPokemonDao {
    @Query("SELECT * FROM '${MyPokemonEntity.TABLE_NAME}' ORDER BY ${MyPokemonEntity.UPDATED_TIME_COLUMN}")
    fun getMyPokemons(): Flow<List<MyPokemonEntity>>

    @Query("SELECT * FROM '${MyPokemonEntity.TABLE_NAME}' WHERE ${MyPokemonEntity.ID_COLUMN} = :id LIMIT 1")
    fun getMyPokemonById(id: String): MyPokemonEntity?

    @Query("DELETE FROM '${MyPokemonEntity.TABLE_NAME}' WHERE ${MyPokemonEntity.ID_COLUMN} = :id")
    suspend fun remove(id: String)

    @Upsert
    suspend fun upsertPokemon(pokemon: MyPokemonEntity)
}