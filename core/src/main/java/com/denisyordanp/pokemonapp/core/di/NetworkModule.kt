package com.denisyordanp.pokemonapp.core.di

import com.denisyordanp.pokemonapp.core.network.ApiService
import com.denisyordanp.pokemonapp.core.network.NetworkConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGsonConverter(): GsonConverterFactory =
        GsonConverterFactory.create(Gson())

    @Provides
    fun provideRetrofit(
        gsonConverter: GsonConverterFactory,
        okHttp: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(gsonConverter)
        .client(okHttp)
        .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            readTimeout(NetworkConfig.TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(NetworkConfig.TIMEOUT, TimeUnit.SECONDS)
            if (NetworkConfig.isDebugMode()) {
                addNetworkInterceptor(loggingInterceptor)
            }
        }.build()

    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}