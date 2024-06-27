package com.denisyordanp.pokemonapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisyordanp.pokemonapp.common.di.IoDispatcher
import com.denisyordanp.pokemonapp.domain.api.CatchPokemon
import com.denisyordanp.pokemonapp.domain.api.CatchProbability
import com.denisyordanp.pokemonapp.domain.api.EditPokemonNickname
import com.denisyordanp.pokemonapp.domain.api.FetchPokemonDetail
import com.denisyordanp.pokemonapp.domain.api.ReleasePokemon
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.denisyordanp.pokemonapp.util.UiState
import com.denisyordanp.pokemonapp.util.UiStatus
import com.denisyordanp.pokemonapp.util.errror
import com.denisyordanp.pokemonapp.util.loadMore
import com.denisyordanp.pokemonapp.util.safeCallWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val fetchDetailPokemon: FetchPokemonDetail,
    private val catchPokemon: CatchPokemon,
    private val catchProbability: CatchProbability,
    private val editPokemonNickname: EditPokemonNickname,
    private val releasePokemon: ReleasePokemon,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _pokemonDetailState = MutableStateFlow<UiState<PokemonDetail>>(UiState())
    val pokemonDetailState = _pokemonDetailState.asStateFlow()

    fun loadPokemonDetail(id: String) = viewModelScope.launch(dispatcher) {
        _pokemonDetailState.update {
            if (it.status != UiStatus.INITIAL) {
                it.loadMore()
            } else it
        }

        safeCallWrapper(
            call = { fetchDetailPokemon(id) },
            onFinish = { _pokemonDetailState.emit(UiState.success(it)) },
            onError = { error -> _pokemonDetailState.update { it.errror(error) } }
        )
    }

    fun tryToCatch() = catchProbability()

    fun catch(pokemon: PokemonDetail) = viewModelScope.launch(dispatcher) {
        safeCallWrapper(
            call = { catchPokemon(pokemon, getCurrentTime()) },
            onFinish = { loadPokemonDetail(pokemon.id) },
            onError = { error -> _pokemonDetailState.update { it.errror(error) } }
        )
    }

    fun editNickname(pokemon: PokemonDetail) = viewModelScope.launch(dispatcher) {
        safeCallWrapper(
            call = { editPokemonNickname(pokemon, getCurrentTime()) },
            onFinish = { loadPokemonDetail(pokemon.id) },
            onError = { error -> _pokemonDetailState.update { it.errror(error) } }
        )
    }

    fun release(id: String) = viewModelScope.launch(dispatcher) {
        safeCallWrapper(
            call = { releasePokemon(id) },
            onFinish = { loadPokemonDetail(id) },
            onError = { error -> _pokemonDetailState.update { it.errror(error) } }
        )
    }

    private fun getCurrentTime() = Date().time
}