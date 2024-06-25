package com.denisyordanp.pokemonapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBarLoading() {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
    )
}