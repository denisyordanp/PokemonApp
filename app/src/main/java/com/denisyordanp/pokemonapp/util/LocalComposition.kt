package com.denisyordanp.pokemonapp.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalSnackBar = compositionLocalOf<SnackbarHostState> {
    error("No LocalSnackBar provided")
}