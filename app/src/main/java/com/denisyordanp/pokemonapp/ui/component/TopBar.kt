package com.denisyordanp.pokemonapp.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    title: String,
    rightButtonIcon: ImageVector,
    showBackButton: Boolean = true,
    showRightButton: Boolean = true,
    onBackPressed: () -> Unit,
    onRightButtonPressed: (() -> Unit)? = null
) {
    Surface(shadowElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 16.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(
                    onClick = onBackPressed
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                modifier = Modifier.weight(1f),
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            if (showRightButton) {
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        onRightButtonPressed?.invoke()
                    }
                ) {
                    Icon(imageVector = rightButtonIcon, contentDescription = null)
                }
            }
        }
    }
}