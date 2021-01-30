package com.javabom.bomkotlin.racing.domain

import java.util.*

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

    fun getRecordsOrderByTimeAsc(): Queue<RaceRecords> {
        val records: Queue<RaceRecords> = LinkedList()
        (1..time).forEach { time ->
            val recordList = racingCars.getRecords(time)
            records.add(RaceRecords(recordList))
        }
        return records
    }

    companion object {
        fun of(racingCars: List<RacingCar>, time: Int): Race {
            return Race(RacingCars(racingCars), time)
        }
    }
}
