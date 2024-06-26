package com.denisyordanp.pokemonapp.ui.screen.mypokemon

import androidx.lifecycle.ViewModel
import com.denisyordanp.pokemonapp.domain.api.LoadMyPokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPokemonViewModel @Inject constructor(
    loadMyPokemonUseCase: LoadMyPokemon
) : ViewModel() {

    val myPokemon = loadMyPokemonUseCase()
}