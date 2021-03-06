package com.javabom.bomkotlin.racing.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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

    @DisplayName("기록을 시간 오름차순으로 정렬해서 가져온다.")
    @Test
    fun findRecordsTest() {
        //given
        val recorder = Recorder.empty()

        val ford1 = RaceRecord(0, "FORD", 1)
        val raceRecords1 = RaceRecords(listOf(ford1))
        val ford2 = RaceRecord(1, "FORD", 2)
        val raceRecords2 = RaceRecords(listOf(ford2))

        recorder.addRecords(raceRecords1)
        recorder.addRecords(raceRecords2)

        //when
        val records = recorder.getRecords()

        //then
        assertThat(records[0]).isEqualTo(raceRecords1)
        assertThat(records[1]).isEqualTo(raceRecords2)
    }
}
