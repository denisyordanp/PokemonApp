package com.denisyordanp.pokemonapp.util

data class UiState<T>(
    val data: T? = null,
    val error: Exception? = null,
    val status: UiStatus = UiStatus.INITIAL
) {
    companion object {
        fun <T> success(data: T) = UiState(data = data, status = UiStatus.SUCCESS)
    }
}

enum class UiStatus {
    SUCCESS, LOAD_MORE, INITIAL, ERROR
}