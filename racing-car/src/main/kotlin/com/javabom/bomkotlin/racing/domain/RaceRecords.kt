package com.javabom.bomkotlin.racing.domain

import kotlin.math.max

class RaceRecords(
    val raceRecords: List<RaceRecord>
) {
    internal fun findLeader(): List<RaceRecord> {
        val maxDistance = raceRecords.maxOf { it.distance }
        return raceRecords.filter { it.distance == maxDistance }
            .toList()
    }

    internal fun getTime(): Int {
        return raceRecords[0].time
    }

}
