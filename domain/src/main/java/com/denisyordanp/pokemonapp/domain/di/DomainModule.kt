package com.denisyordanp.pokemonapp.domain.di

import com.denisyordanp.pokemonapp.domain.api.CatchPokemon
import com.denisyordanp.pokemonapp.domain.api.CatchProbability
import com.denisyordanp.pokemonapp.domain.api.EditPokemonNickname
import com.denisyordanp.pokemonapp.domain.api.FetchPokemonDetail
import com.denisyordanp.pokemonapp.domain.api.FetchPokemons
import com.denisyordanp.pokemonapp.domain.api.LoadMyPokemon
import com.denisyordanp.pokemonapp.domain.api.ReleasePokemon
import com.denisyordanp.pokemonapp.domain.impl.CatchPokemonImpl
import com.denisyordanp.pokemonapp.domain.impl.CatchProbabilityImpl
import com.denisyordanp.pokemonapp.domain.impl.EditPokemonNicknameImpl
import com.denisyordanp.pokemonapp.domain.impl.FetchPokemonDetailImpl
import com.denisyordanp.pokemonapp.domain.impl.FetchPokemonsImpl
import com.denisyordanp.pokemonapp.domain.impl.LoadMyPokemonImpl
import com.denisyordanp.pokemonapp.domain.impl.ReleasePokemonImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindFetchPokemons(
        fetchPokemons: FetchPokemonsImpl
    ): FetchPokemons

    @Binds
    abstract fun bindFetchPokemonDetail(
        fetchPokemonDetail: FetchPokemonDetailImpl
    ): FetchPokemonDetail

    @Binds
    abstract fun bindLoadMyPokemon(
        loadMyPokemon: LoadMyPokemonImpl
    ): LoadMyPokemon

    @Binds
    abstract fun bindCatchPokemon(
        catchPokemon: CatchPokemonImpl
    ): CatchPokemon

    @Binds
    abstract fun bindEditPokemonNickname(
        editPokemonNickname: EditPokemonNicknameImpl
    ): EditPokemonNickname

    @Binds
    abstract fun bindReleasePokemon(
        releasePokemon: ReleasePokemonImpl
    ): ReleasePokemon

    @Binds
    abstract fun bindCatchProbability(
        catchProbability: CatchProbabilityImpl
    ): CatchProbability
}