package com.denisyordanp.pokemonapp.ui.screen.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisyordanp.pokemonapp.common.di.DefaultDispatcher
import com.denisyordanp.pokemonapp.domain.api.FetchPokemons
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.util.UiState
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
class PokemonListViewModel @Inject constructor(
    private val fetchPokemons: FetchPokemons,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _pokemonState = MutableStateFlow<UiState<Paging<Pokemon>>>(UiState())
    val pokemonState = _pokemonState.asStateFlow()

    fun loadPokemons(currentPaging: Paging<Pokemon>? = null) = viewModelScope.launch(dispatcher) {
        if (currentPaging != null) {
            _pokemonState.update {
                it.loadMore()
            }
        }

        try {
            val newPokemons = fetchPokemons(currentPaging?.offset)
            val updatedPokemons = newPokemons.copy(
                data = currentPaging?.data?.plus(newPokemons.data).orEmpty()
            )
            _pokemonState.emit(UiState.success(updatedPokemons))
        } catch (e: Exception) {
            _pokemonState.update {
                it.errror(e)
            }
        }
    }
}