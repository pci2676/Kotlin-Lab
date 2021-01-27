package com.javabom.bomkotlin.racing.domain

class RacingCars(
    private val racingCars: List<RacingCar>
) {
    init {
        validateSize()
    }

    private fun validateSize() {
        if (racingCars.isEmpty()) {
            throw IllegalArgumentException("차량이 없습니다.")
        }
    }

    internal fun move() {
        racingCars.forEach { racingCar -> racingCar.move() }
    }

    internal fun getRecords(time: Int): List<RaceRecord> {
        return racingCars.map { RaceRecord.of(it, time) }
            .toList()
    }
}
