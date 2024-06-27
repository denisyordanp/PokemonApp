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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.denisyordanp.pokemonapp.R
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
                    onShouldShowMessage = {
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
    onShouldShowMessage: (message: String) -> Unit
) {
    val context = LocalContext.current
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
    val currentPokemonAction =
        remember { mutableStateOf<PokemonDetailActionType>(PokemonDetailActionType.Idle) }

    LaunchedEffectKeyed(pokemonDetailState.value) {
        if (it?.status == UiStatus.ERROR) it.error?.apply {
            onShouldShowMessage(context.getString(R.string.error_happening_please_try_again))
        }
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
                    when {
                        it.isMyPokemon -> currentPokemonAction.value =
                            PokemonDetailActionType.Release(it)

                        viewModel.tryToCatch() -> currentPokemonAction.value =
                            PokemonDetailActionType.Catch(it)

                        else -> onShouldShowMessage(
                            context.getString(R.string.sorry_the_pokemon_is_running_away_try_again_later)
                        )
                    }
                },
                onEditNickname = {
                    currentPokemonAction.value = PokemonDetailActionType.EditNickname(it)
                }
            )
        }
    }

    NicknameModal(
        actionType = currentPokemonAction,
        onSubmit = { pokemon ->
            coroutineScope.launch {
                val nickname = pokemon.nickname.orEmpty()

                if (currentPokemonAction.value is PokemonDetailActionType.EditNickname) {
                    viewModel.editNickname(pokemon)
                    onShouldShowMessage(
                        context.getString(
                            R.string.successfully_changed_nickname_to,
                            nickname
                        )
                    )
                } else {
                    viewModel.catch(pokemon)
                    onShouldShowMessage(context.getString(R.string.successfully_caught, nickname))
                }
                currentPokemonAction.value = PokemonDetailActionType.Idle
            }
        }
    )

    ReleaseWarningModal(
        actionType = currentPokemonAction,
        onYes = {
            coroutineScope.launch {
                viewModel.release(it.id)
                onShouldShowMessage(context.getString(R.string.successfully_released, it.name))
                currentPokemonAction.value = PokemonDetailActionType.Idle
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
            text = stringResource(R.string.species, pokemonDetail.species.name.capitalize()),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.base_experience, pokemonDetail.baseExperience),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.height_m, pokemonDetail.height / 10.0),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.weight_kg, pokemonDetail.weight / 10.0),
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
                    Text(text = stringResource(R.string.edit_nickname))
                }
            }

            OutlinedButton(
                modifier = Modifier.padding(16.dp),
                onClick = onMainActionButtonClicked
            ) {
                val text =
                    if (pokemonDetail.isMyPokemon) R.string.release_this_pokemon else R.string.catch_this_pokemon
                Text(text = stringResource(id = text))
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
                    val title = if (isEdit) stringResource(
                        R.string.edit_nickname_for,
                        it.name
                    ) else stringResource(R.string.catch_name, it.name)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {
                        Text(text = stringResource(R.string.enter_nickname))
                    }, value = nicknameText, onValueChange = { nicknameText = it })

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = {
                        onSubmit(it.copy(nickname = nicknameText))
                    }) {
                        val text =
                            if (isEdit) stringResource(R.string.save) else stringResource(R.string.catch_button)
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
                        text = stringResource(R.string.are_you_sure_want_to_release, it.name),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        OutlinedButton(onClick = {
                            actionType.value = PokemonDetailActionType.Idle
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(onClick = {
                            onYes(it)
                        }) {
                            Text(text = stringResource(R.string.yes))
                        }
                    }
                }
            }
        }
    }
}