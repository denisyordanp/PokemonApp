package com.denisyordanp.pokemonapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.denisyordanp.pokemonapp.ui.component.TopBar
import com.denisyordanp.pokemonapp.ui.screen.pokemonlist.PokemonListScreen
import com.denisyordanp.pokemonapp.ui.theme.PokemonAppTheme
import com.denisyordanp.pokemonapp.util.LocalSnackBar
import com.denisyordanp.pokemonapp.util.currentActiveRoute
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
                SetupSystemUI()

                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalSnackBar provides snackBarHostState,
                ) {

                    Scaffold(
                        modifier = Modifier
                            .systemBarsPadding(),
                        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                        topBar = {
                            TopBarContent(
                                navController = navController,
                                onCatchPressed = {
                                    navController.navigate(AppNavigator.Destinations.MY_POKEMON_SCREEN.route)
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
                            startDestination = AppNavigator.Destinations.POKEMON_SCREEN.route
                        ) {
                            composable(
                                route = AppNavigator.Destinations.POKEMON_SCREEN.route,
                            ) {
                                PokemonListScreen(
                                    onItemClicked = {
                                        navController.navigate(AppNavigator.toDetailScreen(it.id))
                                    }
                                )
                            }

                            composable(
                                route = AppNavigator.Destinations.DETAIL_SCREEN.route,
                                arguments = AppNavigator.detailScreenArguments
                            ) { backStack ->
                                backStack.arguments?.getString(AppNavigator.ID_ARGS)
                                    ?.let { id ->
                                        TODO("Detail screen")
                                    }
                            }

                            composable(AppNavigator.Destinations.MY_POKEMON_SCREEN.route) {
                                TODO("My pokemon screen")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SetupSystemUI() {
        val systemUiController = rememberSystemUiController()
        val statusBarColor = MaterialTheme.colorScheme.background
        SideEffect {
            systemUiController.setStatusBarColor(
                color = statusBarColor,
                darkIcons = true,
            )
            systemUiController.setNavigationBarColor(
                color = Color.Black,
                darkIcons = false,
            )
        }
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

    private fun AppNavigator.Destinations?.getTitle() = when (this) {
        AppNavigator.Destinations.POKEMON_SCREEN -> "Pokemons"
        AppNavigator.Destinations.DETAIL_SCREEN -> "Detail Pokemon"
        AppNavigator.Destinations.MY_POKEMON_SCREEN -> "My Pokemons"
        null -> ""
    }

    private val AppNavigator.Destinations?.shouldShowBackButton get() = this == AppNavigator.Destinations.MY_POKEMON_SCREEN
    private val AppNavigator.Destinations?.shouldShowRightButton get() = this == AppNavigator.Destinations.POKEMON_SCREEN
}