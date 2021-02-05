package com.javabom.bomkotlin.racing.ui.console

import com.javabom.bomkotlin.racing.domain.RaceRecord
import com.javabom.bomkotlin.racing.domain.RaceRecords

class RaceOutput {
    companion object {
        fun show(raceRecords: RaceRecords) {
            println("${raceRecords.getTime()} 초")
            for (raceRecord in raceRecords.raceRecords) {
                showDistance(raceRecord)
            }
        }

        private fun showDistance(raceRecord: RaceRecord) {
            print(raceRecord.name + " : ")
            (0..raceRecord.distance).forEach { _ -> print("*") }
            println()
        }
    }
}
