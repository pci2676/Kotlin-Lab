package com.javabom.bomkotlin.racing.domain

import com.javabom.bomkotlin.racing.domain.racingcar.RacingCar

class RaceRecord(
    val distance: Int,
    val name: String,
    val time: Int
) {

    companion object {
        fun of(racingCar: RacingCar, time: Int): RaceRecord {
            return RaceRecord(racingCar.getDistance(), racingCar.name, time)
        }
    }
}
