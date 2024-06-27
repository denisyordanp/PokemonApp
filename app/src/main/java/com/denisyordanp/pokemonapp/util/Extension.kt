package com.denisyordanp.pokemonapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import com.denisyordanp.pokemonapp.ui.main.AppNavigator
import kotlinx.coroutines.CoroutineScope

val NavBackStackEntry.currentActiveRoute: AppNavigator.Destinations?
    get() {
        val route = this.destination.route ?: return null
        return when {
            route == AppNavigator.Destinations.POKEMON_SCREEN.route -> AppNavigator.Destinations.POKEMON_SCREEN
            route == AppNavigator.Destinations.MY_POKEMON_SCREEN.route -> AppNavigator.Destinations.MY_POKEMON_SCREEN
            route.contains(AppNavigator.DETAIL_SCREEN_PATH) -> AppNavigator.Destinations.DETAIL_SCREEN
            else -> throw IllegalStateException("Route doesn't exist: $route")
        }
    }

@Composable
fun <T> LaunchedEffectKeyed(
    key1: T?,
    block: suspend CoroutineScope.(key: T?) -> Unit
) {
    LaunchedEffect(key1 = key1) {
        block(key1)
    }
}

@Composable
fun LaunchedEffectOneTime(
    block: suspend CoroutineScope.() -> Unit
) {
    var hasRun by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (hasRun.not()) {
            block()
            hasRun = true
        }
    }
}

suspend fun <T> safeCallWrapper(
    call: suspend () -> T,
    onFinish: ( suspend (T) -> Unit)? = null,
    onError: suspend (Exception) -> Unit = {},
) {
    try {
        val result = call()
        onFinish?.invoke(result)
    } catch (e: Exception) {
        onError(e)
    }
}

fun <T> UiState<T>.loadMore() = this.copy(error = null, status = UiStatus.LOAD_MORE)
fun <T> UiState<T>.errror(exception: Exception) = this.copy(error = exception, status = UiStatus.ERROR)