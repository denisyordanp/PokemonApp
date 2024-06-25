package com.denisyordanp.pokemonapp.schema.response


import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("base_experience")
    val baseExperience: Int?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("species")
    val species: SpeciesResponse?,
    @SerializedName("weight")
    val weight: Int?
)

data class SpeciesResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)