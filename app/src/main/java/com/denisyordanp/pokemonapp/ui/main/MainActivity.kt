package com.denisyordanp.pokemonapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.denisyordanp.pokemonapp.R
import com.denisyordanp.pokemonapp.ui.component.TopBar
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.Destinations
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.Destinations.DETAIL_SCREEN
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.Destinations.MY_POKEMON_SCREEN
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.Destinations.POKEMON_SCREEN
import com.denisyordanp.pokemonapp.ui.screen.detail.pokemonDetailRoute
import com.denisyordanp.pokemonapp.ui.screen.mypokemon.myPokemonRoute
import com.denisyordanp.pokemonapp.ui.screen.pokemonlist.pokemonListRoute
import com.denisyordanp.pokemonapp.ui.theme.PokemonAppTheme
import com.denisyordanp.pokemonapp.util.LocalCoroutineScope
import com.denisyordanp.pokemonapp.util.LocalNavController
import com.denisyordanp.pokemonapp.util.LocalSnackBar
import com.denisyordanp.pokemonapp.util.currentActiveRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PokemonAppTheme(
                // Will not handle the dark theme for now
                darkTheme = false
            ) {
                PokemonLocalComposition {
                    val snackBarHostState = LocalSnackBar.current
                    val navController = LocalNavController.current

                    Scaffold(
                        modifier = Modifier
                            .systemBarsPadding(),
                        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                        topBar = {
                            TopBarContent(
                                navController = navController,
                                onCatchPressed = {
                                    navController.navigate(MY_POKEMON_SCREEN.route)
                                },
                                onBackPressed = {
                                    navController.navigateUp()
                                }
                            )
                        },
                    ) { padding ->
                        NavHost(
                            modifier = Modifier.padding(padding),
                            navController = navController,
                            startDestination = POKEMON_SCREEN.route
                        ) {
                            pokemonListRoute(this)

                            pokemonDetailRoute(this)

                            myPokemonRoute(this)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun PokemonLocalComposition(
        content: @Composable () -> Unit
    ) {
        val snackBarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()

        CompositionLocalProvider(
            LocalSnackBar provides snackBarHostState,
            LocalNavController provides navController,
            LocalCoroutineScope provides coroutineScope
        ) { content() }
    }

    @Composable
    private fun TopBarContent(
        navController: NavController,
        onCatchPressed: () -> Unit,
        onBackPressed: () -> Unit
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.currentActiveRoute

        TopBar(
            title = currentRoute.getTitle(),
            showBackButton = currentRoute.shouldShowBackButton,
            showRightButton = currentRoute.shouldShowRightButton,
            rightButtonIcon = Icons.Default.Face,
            onRightButtonPressed = onCatchPressed,
            onBackPressed = onBackPressed
        )
    }

    private fun Destinations?.getTitle() = when (this) {
        POKEMON_SCREEN -> getString(R.string.pokemons)
        DETAIL_SCREEN -> getString(R.string.detail_pokemon)
        MY_POKEMON_SCREEN -> getString(R.string.my_pokemons)
        null -> ""
    }

    private val Destinations?.shouldShowBackButton get() = this == MY_POKEMON_SCREEN || this == DETAIL_SCREEN
    private val Destinations?.shouldShowRightButton get() = this == POKEMON_SCREEN
}