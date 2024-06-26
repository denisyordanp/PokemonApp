import com.denisyordanp.pokemonapp.PokemonAndroidConfig
import com.denisyordanp.pokemonapp.PokemonModule

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    id("kotlin-kapt")
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "${PokemonAndroidConfig.NAMESPACE}.${project.path.removePrefix(":").replace(":", ".")}"
    compileSdk = PokemonAndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = PokemonAndroidConfig.MIN_SDK
    }

    compileOptions {
        sourceCompatibility = PokemonAndroidConfig.COMPATIBILITY_VERSION
        targetCompatibility = PokemonAndroidConfig.COMPATIBILITY_VERSION
    }
    kotlinOptions {
        jvmTarget = PokemonAndroidConfig.JVM_TARGET_VERSION
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(PokemonModule.Schema.RESPONSE))
    implementation(project(PokemonModule.Schema.ENTITY))

    implementation(libs.androidx.core.ktx)

    implementation(libs.rerofit.base)
    implementation(libs.rerofit.gson)
    implementation(libs.rerofit.logging)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.compiler)

    testImplementation(libs.junit)
}