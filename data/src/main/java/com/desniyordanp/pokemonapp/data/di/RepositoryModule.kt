package com.desniyordanp.pokemonapp.data.di

import com.desniyordanp.pokemonapp.data.api.ApiRepository
import com.desniyordanp.pokemonapp.data.api.MyPokemonRepository
import com.desniyordanp.pokemonapp.data.impl.ApiRepositoryImpl
import com.desniyordanp.pokemonapp.data.impl.MyPokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindApiRepository(
        apiRepository: ApiRepositoryImpl
    ): ApiRepository

    @Binds
    abstract fun bindMyPokemonRepository(
        myPokemonRepository: MyPokemonRepositoryImpl
    ): MyPokemonRepository
}