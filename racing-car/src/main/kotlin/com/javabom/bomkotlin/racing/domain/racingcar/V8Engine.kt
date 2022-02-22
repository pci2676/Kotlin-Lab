package com.javabom.bomkotlin.racing.domain.racingcar

import kotlin.random.Random

class V8Engine : Engine {
    override fun enoughPower(): Boolean {
        val power = Random.nextInt(11)
        return power > 4
    }
}
