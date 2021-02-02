package com.javabom.bomkotlin.racing.domain

class Recorder {
    private val records = mutableListOf<RaceRecords>()

    internal fun addRecords(raceRecords: RaceRecords): Recorder {
        records.add(raceRecords)
        return this
    }

    internal fun findWinner(): List<RaceRecord> {
        records.sortByDescending { it.getTime() }
        return records[0].findLeader()
    }

    internal fun findRecords(time: Int): RaceRecords {
        return records.first { it.getTime() == time }
    }

    companion object {
        fun empty(): Recorder {
            return Recorder()
        }
    }
}
