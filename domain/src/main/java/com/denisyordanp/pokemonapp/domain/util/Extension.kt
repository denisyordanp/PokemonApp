package com.denisyordanp.pokemonapp.domain.util

import java.net.URI

fun String.getIdFromPokemonUrl(): String {
    val parts = this.split("/")
    return parts.getOrNull(6).orEmpty()
}

fun String?.getOffsetFromUrl(): Int {
    if (this.isNullOrEmpty()) return 0

    val uri = URI(this)
    val query = uri.query ?: return 0

    val params = query.split("&")
    for (param in params) {
        val keyValue = param.split("=")
        if (keyValue.size == 2 && keyValue[0] == "offset") {
            return try {
                keyValue[1].toInt()
            } catch (e: NumberFormatException) { 0 }
        }
    }
    return 0
}