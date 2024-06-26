package com.denisyordanp.pokemonapp.core.network

import com.denisyordanp.pokemonapp.core.BuildConfig

object NetworkConfig {
    fun isDebugMode(): Boolean = BuildConfig.DEBUG
    const val BASE_URL: String = "https://pokeapi.co/api/v2/"
    const val TIMEOUT: Long = 60
}