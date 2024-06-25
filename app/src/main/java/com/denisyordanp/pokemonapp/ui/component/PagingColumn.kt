package com.denisyordanp.pokemonapp.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denisyordanp.pokemonapp.util.LaunchedEffectOneTime

@Composable
fun PagingColumn(
    modifier: Modifier,
    onNeedLoadMore: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        state = lazyListState
    ) {
        content()
        item {
            LaunchedEffectOneTime { onNeedLoadMore() }
        }
    }
}