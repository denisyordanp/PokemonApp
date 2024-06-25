package com.denisyordanp.pokemonapp.schema.response


import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<ResultResponse>?
)

data class ResultResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)