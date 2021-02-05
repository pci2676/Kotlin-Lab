package com.javabom.bomkotlin.racing.domain

import com.javabom.bomkotlin.racing.domain.racingcar.RacingCar

class Race(
    private val racingCars: RacingCars,
    private val time: Int
) {
    private val recorder = Recorder.empty()

    fun start() {
        (1..time).forEach { time ->
            racingCars.move()
            val records = racingCars.getRecords(time)
            recorder.addRecords(RaceRecords(records))
        }
    }

    fun getRecordsOrderByTimeAsc(): List<RaceRecords> {
        return recorder.getRecords()
    }

    companion object {
        fun of(racingCars: List<RacingCar>, time: Int): Race {
            return Race(RacingCars(racingCars), time)
        }
    }
}
