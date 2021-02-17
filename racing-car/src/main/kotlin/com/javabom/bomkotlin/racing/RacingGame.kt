package com.javabom.bomkotlin.racing

import com.javabom.bomkotlin.racing.domain.Race
import com.javabom.bomkotlin.racing.domain.racingcar.V8Engine
import com.javabom.bomkotlin.racing.ui.console.RaceCommand
import com.javabom.bomkotlin.racing.ui.console.RaceOutput

class RacingGame {
    fun run() {
        val racerRequest = RaceCommand.inputRacerNames()
        val time = RaceCommand.inputTime()
            .time

        val racingCars = racerRequest.toRacingCarCreator()
            .toRacingCar(V8Engine())

        val race = Race.of(racingCars, time)
        race.start()

        val raceRecordsBundle = race.getRecordsOrderByTimeAsc()

        for (raceRecords in raceRecordsBundle) {
            RaceOutput.show(raceRecords)
        }
    }

    companion object {
        fun run() {
            RacingGame().run()
        }
    }
}
