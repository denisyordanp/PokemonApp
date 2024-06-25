package com.denisyordanp.pokemonapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PokemonItem(
    modifier: Modifier = Modifier,
    name: String,
    nickname: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        nickname?.let {
            Text(
                modifier = Modifier.weight(1f),
                text = it,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}