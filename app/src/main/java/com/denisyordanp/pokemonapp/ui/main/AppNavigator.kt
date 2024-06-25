package com.denisyordanp.pokemonapp.ui.main

import androidx.navigation.NavType
import androidx.navigation.navArgument

object AppNavigator {
    const val ID_ARGS = "username"
    const val DETAIL_SCREEN_PATH = "detail"

    fun toDetailScreen(id: String) = "$DETAIL_SCREEN_PATH/$id"

    val detailScreenArguments = listOf(
        navArgument(name = ID_ARGS) {
            type = NavType.StringType
        }
    )

    enum class Destinations(val route: String) {
        POKEMON_SCREEN("pokemon"),
        DETAIL_SCREEN("$DETAIL_SCREEN_PATH/{$ID_ARGS}"),
        MY_POKEMON_SCREEN("my_pokemon_screen")
    }
}