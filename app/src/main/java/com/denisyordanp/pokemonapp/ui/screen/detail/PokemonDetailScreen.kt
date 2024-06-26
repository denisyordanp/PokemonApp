package com.denisyordanp.pokemonapp.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.denisyordanp.pokemonapp.ui.component.CenterLoading
import com.denisyordanp.pokemonapp.ui.component.TopBarLoading
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.Destinations.DETAIL_SCREEN
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.ID_ARGS
import com.denisyordanp.pokemonapp.ui.main.AppNavigator.detailScreenArguments
import com.denisyordanp.pokemonapp.util.LaunchedEffectKeyed
import com.denisyordanp.pokemonapp.util.LaunchedEffectOneTime
import com.denisyordanp.pokemonapp.util.LocalCoroutineScope
import com.denisyordanp.pokemonapp.util.LocalSnackBar
import com.denisyordanp.pokemonapp.util.UiStatus
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.util.Locale

fun pokemonDetailRoute(
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.apply {
        composable(
            route = DETAIL_SCREEN.route,
            arguments = detailScreenArguments
        ) { backStack ->
            val snackBar = LocalSnackBar.current
            val coroutineScope = LocalCoroutineScope.current

            backStack.arguments?.getString(ID_ARGS)?.let {
                PokemonDetailScreen(
                    id = it,
                    onError = {
                        coroutineScope.launch {
                            snackBar.showSnackbar("Error happening, please try again.")
                        }
                    },
                    onCaught = {
                        coroutineScope.launch {
                            snackBar.showSnackbar("$it successfully caught")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    id: String,
    onCaught: (nickname: String) -> Unit,
    onError: (e: Exception) -> Unit,
) {
    val coroutineScope = LocalCoroutineScope.current

    LaunchedEffectOneTime {
        viewModel.loadPokemonDetail(id)
    }

    val pokemonDetailState = viewModel.pokemonDetailState.collectAsState()
    val pokemonDetail by remember {
        derivedStateOf {
            pokemonDetailState.value.data
        }
    }
    val isLoadMoreDataLoadingMore by remember {
        derivedStateOf {
            pokemonDetailState.value.status == UiStatus.LOAD_MORE
        }
    }
    val isInitial by remember {
        derivedStateOf {
            pokemonDetailState.value.status == UiStatus.INITIAL
        }
    }
    val refreshState = rememberSwipeRefreshState(isRefreshing = isLoadMoreDataLoadingMore)
    val selectedCatchPokemon = remember { mutableStateOf<PokemonDetailActionType>(PokemonDetailActionType.Idle) }

    LaunchedEffectKeyed(pokemonDetailState.value) {
        if (it?.status == UiStatus.ERROR) it.error?.apply(onError)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoadMoreDataLoadingMore) {
            TopBarLoading()
        }

        if (isInitial) {
            CenterLoading()
        }

        pokemonDetail?.let {
            SwipeRefresh(
                state = refreshState,
                onRefresh = { viewModel.loadPokemonDetail(id) }
            ) {
                PokemonDetailContent(
                    pokemonDetail = it,
                    onMainActionButtonClicked = {
                        selectedCatchPokemon.value = if (it.isMyPokemon) {
                            PokemonDetailActionType.Release(it)
                        } else PokemonDetailActionType.Catch(it)
                    },
                    onEditNickname = {
                        selectedCatchPokemon.value = PokemonDetailActionType.EditNickname(it)
                    }
                )
            }
        }
    }

    NicknameModal(
        actionType = selectedCatchPokemon,
        onCatch = { pokemon, nickname ->
            coroutineScope.launch {
                viewModel.catch(pokemon, nickname)
                selectedCatchPokemon.value = PokemonDetailActionType.Idle
                onCaught(nickname)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NicknameModal(
    actionType: MutableState<PokemonDetailActionType>,
    onCatch: (pokemon: PokemonDetail, nickname: String) -> Unit,
) = with(actionType.value) {
    pokemon?.let {
        var nicknameText by remember { mutableStateOf(actionType.value.pokemon?.nickname.orEmpty()) }

        ModalBottomSheet(
            onDismissRequest = {
                nicknameText = ""
                actionType.value = PokemonDetailActionType.Idle
            },
            windowInsets = WindowInsets.ime
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Catch ${it.name}")

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {
                    Text(text = "Enter nickname")
                }, value = nicknameText, onValueChange = { nicknameText = it })

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(onClick = {
                    onCatch(it, nicknameText)
                }) {
                    Text(text = "Catch")
                }
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(
    pokemonDetail: PokemonDetail,
    onMainActionButtonClicked: () -> Unit,
    onEditNickname: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = pokemonDetail.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Species: ${
                pokemonDetail.species.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Base Experience: ${pokemonDetail.baseExperience}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Height: ${pokemonDetail.height / 10.0} m",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Weight: ${pokemonDetail.weight / 10.0} kg",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            if (pokemonDetail.isMyPokemon) {
                OutlinedButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = onEditNickname
                ) {
                    Text(text = "Edit nickname")
                }
            }

            OutlinedButton(
                modifier = Modifier.padding(16.dp),
                onClick = onMainActionButtonClicked
            ) {
                Text(
                    text = if (pokemonDetail.isMyPokemon) "Release this Pokemon" else "Catch this Pokemon"
                )
            }
        }
    }
}