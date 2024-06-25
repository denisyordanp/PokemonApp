package com.denisyordanp.pokemonapp.domain.mapper

import com.denisyordanp.pokemonapp.domain.util.getIdFromPokemonUrl
import com.denisyordanp.pokemonapp.domain.util.getOffsetFromUrl
import com.denisyordanp.pokemonapp.schema.response.PokemonListResponse
import com.denisyordanp.pokemonapp.schema.response.PokemonResponse
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon


fun PokemonListResponse.toPokemonPaging(): Paging<Pokemon> {
    val offset = this.next.getOffsetFromUrl()
    val pokemons = this.results?.map {
        it.toPokemon()
    } ?: emptyList()

    return Paging(offset, pokemons)
}

fun PokemonResponse.toPokemon(): Pokemon {
    return Pokemon(
        id = url?.getIdFromPokemonUrl().orEmpty(),
        name = name.orEmpty()
    )
}

