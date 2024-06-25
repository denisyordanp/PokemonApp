package com.denisyordanp.pokemonapp.ui.screen.pokemonlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.ui.component.CenterLoading
import com.denisyordanp.pokemonapp.ui.component.PagingColumn
import com.denisyordanp.pokemonapp.ui.component.PokemonItem
import com.denisyordanp.pokemonapp.ui.component.TopBarLoading
import com.denisyordanp.pokemonapp.util.LaunchedEffectKeyed
import com.denisyordanp.pokemonapp.util.LaunchedEffectOneTime
import com.denisyordanp.pokemonapp.util.LocalSnackBar
import com.denisyordanp.pokemonapp.util.UiStatus

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    onItemClicked: (pokemon: Pokemon) -> Unit
) {
    val snackBar = LocalSnackBar.current

    LaunchedEffectOneTime {
        viewModel.loadPokemons()
    }

    val pokemonsState = viewModel.pokemonState.collectAsState()
    val pokemons by remember {
        derivedStateOf {
            pokemonsState.value.data
        }
    }
    val isLoadMoreDataLoadingMore by remember {
        derivedStateOf {
            pokemonsState.value.status == UiStatus.LOAD_MORE
        }
    }
    val isInitial by remember {
        derivedStateOf {
            pokemonsState.value.status == UiStatus.INITIAL
        }
    }

    LaunchedEffectKeyed(key1 = pokemonsState.value) {
        if (it?.status == UiStatus.ERROR) {
            snackBar.showSnackbar("Error happening, please try again.")
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoadMoreDataLoadingMore) {
            TopBarLoading()
        }

        if (isInitial) {
            CenterLoading()
        }

        pokemons?.let {
            PokemonContent(
                paging = it,
                onNeedLoadMore = {
                    viewModel.loadPokemons(it)
                }
            )
        }
    }
}


@Composable
private fun PokemonContent(
    paging: Paging<Pokemon>,
    onNeedLoadMore: () -> Unit
) {
    PagingColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        onNeedLoadMore = onNeedLoadMore
    ) {
        items(items = paging.data, key = { "${it.id}${it.name}" }) {
            PokemonItem(name = it.name)
        }
    }
}