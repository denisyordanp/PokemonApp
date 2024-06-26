package com.denisyordanp.pokemonapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.FetchPokemonDetail
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.denisyordanp.pokemonapp.util.UiState
import com.denisyordanp.pokemonapp.util.UiStatus
import com.denisyordanp.pokemonapp.util.errror
import com.denisyordanp.pokemonapp.util.loadMore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val fetchDetailPokemon: FetchPokemonDetail,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _pokemonDetailState = MutableStateFlow<UiState<PokemonDetail>>(UiState())
    val pokemonDetailState = _pokemonDetailState.asStateFlow()

    fun loadPokemonDetail(id: String) = viewModelScope.launch(dispatcher) {
        _pokemonDetailState.update {
            if (it.status != UiStatus.INITIAL) { it.loadMore() } else it
        }

        try {
            val pokemonDetail = fetchDetailPokemon(id)
            _pokemonDetailState.emit(UiState.success(pokemonDetail))
        } catch (e: Exception) {
            _pokemonDetailState.update { it.errror(e) }
        }
    }

    fun catchPokemon(pokemon: PokemonDetail) = viewModelScope.launch(dispatcher) {

    }
}