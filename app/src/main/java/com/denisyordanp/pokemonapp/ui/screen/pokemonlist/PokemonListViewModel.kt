package com.denisyordanp.pokemonapp.ui.screen.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisyordanp.pokemonapp.common.di.DefaultDispatcher
import com.denisyordanp.pokemonapp.domain.api.FetchPokemons
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.util.UiState
import com.denisyordanp.pokemonapp.util.UiStatus
import com.denisyordanp.pokemonapp.util.error
import com.denisyordanp.pokemonapp.util.loadMore
import com.denisyordanp.pokemonapp.util.safeCallWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val fetchPokemons: FetchPokemons,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _pokemonState = MutableStateFlow<UiState<Paging<Pokemon>>>(UiState())
    val pokemonState = _pokemonState.asStateFlow()

    fun loadPokemons(isRefresh: Boolean = false) = viewModelScope.launch(dispatcher) {
        _pokemonState.update {
            if (it.status != UiStatus.INITIAL) {
                it.loadMore()
            } else it
        }

        val currentPaging = pokemonState.value.data

        safeCallWrapper(
            call = {
                if (isRefresh) fetchPokemons() else fetchPokemons(currentPaging?.offset)
            },
            onFinish = {
                val updatedPokemons = if (isRefresh) {
                    it
                } else {
                    val newPokemons = fetchPokemons(currentPaging?.offset)
                    newPokemons.copy(
                        data = currentPaging?.data?.plus(newPokemons.data).orEmpty()
                    )
                }

                _pokemonState.emit(UiState.success(updatedPokemons))
            },
            onError = { error -> _pokemonState.update { it.error(error) } }
        )
    }
}