package com.denisyordanp.pokemonapp.ui.screen.mypokemon

import androidx.lifecycle.ViewModel
import com.denisyordanp.pokemonapp.common.di.DefaultDispatcher
import com.denisyordanp.pokemonapp.domain.api.LoadMyPokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MyPokemonViewModel @Inject constructor(
    loadMyPokemonUseCase: LoadMyPokemon,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val myPokemon = loadMyPokemonUseCase().flowOn(dispatcher)
}