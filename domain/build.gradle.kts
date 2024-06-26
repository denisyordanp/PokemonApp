import com.denisyordanp.pokemonapp.PokemonAndroidConfig
import com.denisyordanp.pokemonapp.PokemonModule

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
    implementation(project(PokemonModule.Main.DATA))
    implementation(project(PokemonModule.Main.COMMON))
    implementation(project(PokemonModule.Schema.RESPONSE))
    implementation(project(PokemonModule.Schema.UI))
    implementation(project(PokemonModule.Schema.ENTITY))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.compiler)

    testImplementation(libs.junit)
}