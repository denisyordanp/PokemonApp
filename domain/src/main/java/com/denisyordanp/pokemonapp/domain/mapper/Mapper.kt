package com.denisyordanp.pokemonapp.domain.mapper

import com.denisyordanp.pokemonapp.common.extension.orZero
import com.denisyordanp.pokemonapp.domain.util.getIdFromPokemonUrl
import com.denisyordanp.pokemonapp.domain.util.getOffsetFromUrl
import com.denisyordanp.pokemonapp.schema.entity.MyPokemonEntity
import com.denisyordanp.pokemonapp.schema.response.PokemonDetailResponse
import com.denisyordanp.pokemonapp.schema.response.PokemonListResponse
import com.denisyordanp.pokemonapp.schema.response.PokemonResponse
import com.denisyordanp.pokemonapp.schema.response.SpeciesResponse
import com.denisyordanp.pokemonapp.schema.ui.Paging
import com.denisyordanp.pokemonapp.schema.ui.Pokemon
import com.denisyordanp.pokemonapp.schema.ui.PokemonDetail
import com.denisyordanp.pokemonapp.schema.ui.Species


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

fun PokemonDetailResponse.toPokemonDetail(isMyPokemon: Boolean, nickname: String?): PokemonDetail {
    return PokemonDetail(
        id = id.orEmpty(),
        name = name.orEmpty(),
        height = height.orZero(),
        weight = weight.orZero(),
        baseExperience = baseExperience.orZero(),
        species = species.toSpecies(),
        isMyPokemon = isMyPokemon,
        nickname = nickname
    )
}

fun SpeciesResponse?.toSpecies(): Species {
    return Species(
        name = this?.name.orEmpty()
    )
}

fun MyPokemonEntity.toPokemon(): Pokemon {
    return Pokemon(id = id, name = name, nickname = nickname)
}

fun PokemonDetail.toMyPokemonEntity(currentTime: Long): MyPokemonEntity {
    return MyPokemonEntity(
        id = id,
        name = name,
        nickname = nickname.orEmpty(),
        updatedTime = currentTime
    )
}

