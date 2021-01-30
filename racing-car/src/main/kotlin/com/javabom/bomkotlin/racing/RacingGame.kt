package com.javabom.bomkotlin.racing

import com.javabom.bomkotlin.racing.domain.Race
import com.javabom.bomkotlin.racing.domain.V8Engine
import com.javabom.bomkotlin.racing.ui.console.RaceCommand

class RacingGame {
    fun run() {
        val racerRequest = RaceCommand.inputRacerNames()
        val racingCars = racerRequest.toRacingCarCreator()
            .toRacingCar(V8Engine())
        val time = RaceCommand.inputTime()
            .time

        val race = Race.of(racingCars, time)
        race.start()
        val recordsOrderByTime = race.getRecordsOrderByTimeAsc()

    }

    companion object {
        fun run() {
            RacingGame().run()
        }
    }
}
