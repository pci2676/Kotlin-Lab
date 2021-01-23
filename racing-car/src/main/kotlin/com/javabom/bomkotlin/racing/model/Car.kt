package com.javabom.bomkotlin.racing.model

import com.javabom.bomkotlin.racing.model.Position.Companion.zero

class Car(
    val name: String,
    private val engine: Engine
) {
    private var position: Position = zero()

    init {
        if (name.trim().isBlank()) {
            throw IllegalArgumentException("이름은 공백으로 생성할 수 없습니다.")
        }
    }

    internal fun move(): Car {
        if (this.engine.enoughPower()) {
            this.position = this.position.forward()
        }
        return this
    }

    internal fun getDistance(): Int {
        return this.position.distance
    }
}
