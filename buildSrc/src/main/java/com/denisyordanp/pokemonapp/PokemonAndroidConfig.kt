package com.denisyordanp.pokemonapp

import org.gradle.api.JavaVersion

object PokemonAndroidConfig {
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 24
    const val JVM_TARGET_VERSION = "1.8"
    const val NAMESPACE = "com.denisyordanp.pokemonapp"
    const val COMPOSE_OPTION_VERSION = "1.5.1"
    val COMPATIBILITY_VERSION = JavaVersion.VERSION_1_8
}

