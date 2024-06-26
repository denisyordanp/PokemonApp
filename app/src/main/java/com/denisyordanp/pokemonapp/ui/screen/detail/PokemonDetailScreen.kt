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
import androidx.compose.foundation.layout.width
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
import kotlinx.coroutines.launch

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
                    onSuccessAction = {
                        coroutineScope.launch {
                            snackBar.showSnackbar(it)
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
    onSuccessAction: (message: String) -> Unit,
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
    val selectedCatchPokemon =
        remember { mutableStateOf<PokemonDetailActionType>(PokemonDetailActionType.Idle) }

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

    NicknameModal(
        actionType = selectedCatchPokemon,
        onSubmit = { pokemon ->
            coroutineScope.launch {
                val nickname = pokemon.nickname.orEmpty()

                if (selectedCatchPokemon.value is PokemonDetailActionType.EditNickname) {
                    viewModel.editNickname(pokemon)
                    onSuccessAction("Successfully changed nickname to $nickname")
                } else {
                    viewModel.catch(pokemon)
                    onSuccessAction("$nickname successfully caught")
                }
                selectedCatchPokemon.value = PokemonDetailActionType.Idle
            }
        }
    )

    ReleaseWarningModal(
        actionType = selectedCatchPokemon,
        onYes = {
            coroutineScope.launch {
                viewModel.release(it.id)
                onSuccessAction("${it.name} successfully released")
                selectedCatchPokemon.value = PokemonDetailActionType.Idle
            }
        }
    )
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
        pokemonDetail.nickname?.let {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = it.capitalize(),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = pokemonDetail.name.capitalize(),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Species: ${pokemonDetail.species.name.capitalize()}",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NicknameModal(
    actionType: MutableState<PokemonDetailActionType>,
    onSubmit: (pokemon: PokemonDetail) -> Unit,
) = with(actionType.value) {
    if (this is PokemonDetailActionType.Catch || this is PokemonDetailActionType.EditNickname) {
        pokemon?.let {
            val isEdit by remember {
                derivedStateOf { this is PokemonDetailActionType.EditNickname }
            }
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
                    val title = if (isEdit) "Edit nickname for ${it.name}" else "Catch ${it.name}"
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {
                        Text(text = "Enter nickname")
                    }, value = nicknameText, onValueChange = { nicknameText = it })

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = {
                        onSubmit(it.copy(nickname = nicknameText))
                    }) {
                        val text = if (isEdit) "Save" else "Catch"
                        Text(text = text)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReleaseWarningModal(
    actionType: MutableState<PokemonDetailActionType>,
    onYes: (pokemon: PokemonDetail) -> Unit
) {
    if (actionType.value is PokemonDetailActionType.Release) {
        actionType.value.pokemon?.let {
            ModalBottomSheet(
                onDismissRequest = {
                    actionType.value = PokemonDetailActionType.Idle
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Are you sure want to release ${it.name}}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        OutlinedButton(onClick = {
                            actionType.value = PokemonDetailActionType.Idle
                        }) {
                            Text(text = "Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(onClick = {
                            onYes(it)
                        }) {
                            Text(text = "Yes")
                        }
                    }
                }
            }
        }
    }
}