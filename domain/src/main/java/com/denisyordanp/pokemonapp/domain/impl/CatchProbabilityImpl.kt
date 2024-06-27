package com.denisyordanp.pokemonapp.domain.impl

import com.denisyordanp.pokemonapp.domain.api.CatchProbability
import javax.inject.Inject
import kotlin.random.Random

class CatchProbabilityImpl @Inject constructor()  : CatchProbability {
    override fun invoke() = Random.nextBoolean()
}