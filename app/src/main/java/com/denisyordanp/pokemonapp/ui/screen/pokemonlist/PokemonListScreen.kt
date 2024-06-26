package com.denisyordanp.pokemonapp.ui.screen.pokemonlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.ui.component.CenterLoading
import com.denisyordanp.pokemonapp.ui.component.PagingColumn
import com.denisyordanp.pokemonapp.ui.component.PokemonItem
import com.denisyordanp.pokemonapp.ui.component.TopBarLoading
import com.denisyordanp.pokemonapp.ui.main.AppNavigator
import com.denisyordanp.pokemonapp.util.LaunchedEffectKeyed
import com.denisyordanp.pokemonapp.util.LaunchedEffectOneTime
import com.denisyordanp.pokemonapp.util.LocalCoroutineScope
import com.denisyordanp.pokemonapp.util.LocalNavController
import com.denisyordanp.pokemonapp.util.LocalSnackBar
import com.denisyordanp.pokemonapp.util.UiStatus
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

fun pokemonListRoute(
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.apply {
        composable(
            route = AppNavigator.Destinations.POKEMON_SCREEN.route,
        ) {
            val navController = LocalNavController.current
            val snackBar = LocalSnackBar.current
            val coroutineScope = LocalCoroutineScope.current

            PokemonListScreen(
                onItemClicked = {
                    navController.navigate(AppNavigator.toDetailScreen(it.id))
                },
                onError = {
                    coroutineScope.launch {
                        snackBar.showSnackbar("Error happening, please try again.")
                    }
                }
            )
        }
    }
}

@Composable
private fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    onError: (e: Exception) -> Unit,
    onItemClicked: (pokemon: Pokemon) -> Unit
) {
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
    val refreshState = rememberSwipeRefreshState(isRefreshing = isLoadMoreDataLoadingMore)

    LaunchedEffectKeyed(pokemonsState.value) {
        if (it?.status == UiStatus.ERROR) it.error?.apply(onError)
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
            SwipeRefresh(
                state = refreshState,
                onRefresh = { viewModel.loadPokemons(true) }
            ) {
                PokemonListContent(
                    paging = it,
                    onNeedLoadMore = { viewModel.loadPokemons() },
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}


@Composable
private fun PokemonListContent(
    paging: Paging<Pokemon>,
    onNeedLoadMore: () -> Unit,
    onItemClicked: (pokemon: Pokemon) -> Unit
) {
    PagingColumn(
        onNeedLoadMore = onNeedLoadMore
    ) {
        items(items = paging.data, key = { "${it.id}${it.name}" }) {
            PokemonItem(name = it.name, onClick = { onItemClicked(it) })
        }
    }
}