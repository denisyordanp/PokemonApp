package com.denisyordanp.pokemonapp.core.di

import android.content.Context
import com.denisyordanp.pokemonapp.core.database.AppDatabase
import com.denisyordanp.pokemonapp.core.database.dao.MyPokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideMyPokemonDao(appDatabase: AppDatabase): MyPokemonDao = appDatabase.myPokemonDao()
}