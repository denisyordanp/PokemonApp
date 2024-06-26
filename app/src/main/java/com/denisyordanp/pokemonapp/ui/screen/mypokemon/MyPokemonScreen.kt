package com.denisyordanp.pokemonapp.ui.screen.mypokemon

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.ui.component.PokemonItem
import com.denisyordanp.pokemonapp.ui.main.AppNavigator
import com.denisyordanp.pokemonapp.util.LocalNavController

fun myPokemonRoute(
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.apply {
        composable(
            route = AppNavigator.Destinations.MY_POKEMON_SCREEN.route,
        ) {
            val navController = LocalNavController.current

            MyPokemonScreen(
                onItemClicked = {
                    navController.navigate(AppNavigator.toDetailScreen(it.id))
                }
            )
        }
    }
}

@Composable
private fun MyPokemonScreen(
    viewModel: MyPokemonViewModel = hiltViewModel(),
    onItemClicked: (pokemon: Pokemon) -> Unit
) {
    val myPokemons = viewModel.myPokemon.collectAsState(emptyList())

    LazyColumn {
        items(items = myPokemons.value, key = { "${it.id}${it.name}" }) {
            PokemonItem(name = it.name, nickname = it.nickname, onClick = { onItemClicked(it) })
        }
    }
}