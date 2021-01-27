package com.javabom.bomkotlin.racing.domain

import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName

internal class RecorderTest {

    @DisplayName("가장 시간이 늦은 레코드중 1등을 찾는다.")
    @Test
    fun findWinnerTest() {
        //given
        val raceRecord1 = RaceRecord(1, "FORD", 0)
        val raceRecord2 = RaceRecord(1, "FERRARI", 0)
        val raceRecords1 = RaceRecords(listOf(raceRecord1, raceRecord2))

        val raceRecord3 = RaceRecord(2, "FORD", 1)
        val raceRecord4 = RaceRecord(1, "FERRARI", 1)
        val raceRecords2 = RaceRecords(listOf(raceRecord3, raceRecord4))

        val recorder = Recorder.empty()
        recorder.addRecords(raceRecords1)
            .addRecords(raceRecords2)

        //when
        val winner = recorder.findWinner()

        //then
        assertThat(winner).isEqualTo(listOf(raceRecord3))
    }
}
