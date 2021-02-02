package com.javabom.bomkotlin.racing.domain

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
        val records = mutableListOf<RaceRecords>()
        (1..time).forEach { time ->
            records.add(recorder.findRecords(time))
        }
        return records
    }

    companion object {
        fun of(racingCars: List<RacingCar>, time: Int): Race {
            return Race(RacingCars(racingCars), time)
        }
    }
}
