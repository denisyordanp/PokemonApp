import com.denisyordanp.pokemonapp.PokemonAndroidConfig
import com.denisyordanp.pokemonapp.PokemonModule

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.android.hilt)
}

android {
    namespace = PokemonAndroidConfig.NAMESPACE
    compileSdk = PokemonAndroidConfig.COMPILE_SDK

    defaultConfig {
        applicationId = PokemonAndroidConfig.NAMESPACE
        minSdk = PokemonAndroidConfig.MIN_SDK
        targetSdk = PokemonAndroidConfig.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = PokemonAndroidConfig.COMPATIBILITY_VERSION
        targetCompatibility = PokemonAndroidConfig.COMPATIBILITY_VERSION
    }
    kotlinOptions {
        jvmTarget = PokemonAndroidConfig.JVM_TARGET_VERSION
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = PokemonAndroidConfig.COMPOSE_OPTION_VERSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(PokemonModule.Schema.UI))
    implementation(project(PokemonModule.Main.DOMAIN))
    implementation(project(PokemonModule.Main.COMMON))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.coroutine)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.compiler)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.accompanist.systemui)
    implementation(libs.androidx.accompanist.swiperefresh)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}