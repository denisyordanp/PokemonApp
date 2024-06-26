package com.denisyordanp.pokemonapp.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

val LocalSnackBar = staticCompositionLocalOf<SnackbarHostState> {
    error("No LocalSnackBar provided")
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}

val LocalCoroutineScope = staticCompositionLocalOf<CoroutineScope> {
    error("No LocalCoroutineScope provided")
}