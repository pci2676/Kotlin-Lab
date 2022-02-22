package com.javabom.bomkotlin.racing.domain.racingcar

import com.javabom.bomkotlin.racing.domain.RacingCars
import com.javabom.bomkotlin.racing.domain.racingcar.Position.Companion.zero

class RacingCar(
    val name: String,
    private val engine: Engine,
) {
    private var position: Position = zero()

    init {
        if (name.trim().isBlank()) {
            throw IllegalArgumentException("이름은 공백으로 생성할 수 없습니다.")
        }
    }

    internal fun move(): RacingCar {
        if (this.engine.enoughPower()) {
            this.position = this.position.forward()
        }
        return this
    }

    internal fun getDistance(): Int {
        return this.position.distance
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RacingCar

        if (name != other.name) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }

}

fun Iterable<RacingCar>.toFirstClassCollection(): RacingCars {
    return RacingCars(this.toList())
}
