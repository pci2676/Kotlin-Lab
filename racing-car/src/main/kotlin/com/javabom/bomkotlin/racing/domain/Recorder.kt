package com.javabom.bomkotlin.racing.domain

class Recorder {
    val records = mutableListOf<RaceRecords>()

    internal fun addRecords(raceRecords: RaceRecords): Recorder {
        records.add(raceRecords)
        return this
    }

    internal fun findWinner(): List<RaceRecord> {
        records.sortByDescending { it.getTime() }
        return records[0].findLeader()
    }

    companion object {
        fun empty(): Recorder {
            return Recorder()
        }
    }
}
