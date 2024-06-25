package com.desniyordanp.pokemonapp.data.di

import com.desniyordanp.pokemonapp.data.api.ApiRepository
import com.desniyordanp.pokemonapp.data.impl.ApiRepositoryImpl
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
}