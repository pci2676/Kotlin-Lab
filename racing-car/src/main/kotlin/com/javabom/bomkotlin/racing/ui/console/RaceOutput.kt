package com.javabom.bomkotlin.racing.ui.console

import com.javabom.bomkotlin.racing.domain.RaceRecord
import com.javabom.bomkotlin.racing.domain.RaceRecords

class RaceOutput {
    companion object {
        fun show(raceRecords: RaceRecords) {
            println("${raceRecords.getTime()} 초")
            repeat(raceRecords.raceRecords.count()) { showDistance(raceRecords.raceRecords[it]) }
        }

        private fun showDistance(raceRecord: RaceRecord) {
            println(raceRecord.name + " : " + makeDistance(raceRecord.distance))
        }
    }
}

fun makeDistance(distance: Int): String = with(StringBuilder()) {
    repeat(distance) { append("*") }
    toString()
}
