import com.denisyordanp.pokemonapp.PokemonAndroidConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.compiler)
}