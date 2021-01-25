package com.javabom.bomkotlin.racing.domain.creator

import com.javabom.bomkotlin.racing.domain.Engine
import com.javabom.bomkotlin.racing.domain.RacingCar

class RacingCarCreator(
    val names: List<String>
) {
    init {
        validateNames()
    }

    fun validateNames() {
        val invalid = names.isEmpty() || names.any { name -> name.trim().isBlank() }

        if (invalid) {
            throw IllegalArgumentException("비어있는 이름이 있습니다. : $names")
        }
    }

    fun toRacingCar(engine: Engine): List<RacingCar> {
        return this.names.map { name -> RacingCar(name, engine) }
            .toList()
    }
}
