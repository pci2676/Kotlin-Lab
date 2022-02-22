package com.javabom.bomkotlin.racing.domain

import com.javabom.bomkotlin.racing.domain.racingcar.RacingCar
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.LinkedList
import java.util.Queue
import java.util.stream.Stream

internal class RaceRecordsTest {

    companion object {
        @JvmStatic
        fun findLeaderTestProvider(): Stream<Arguments> {
            val raceRecord1 = RaceRecord(1, "FORD", 1)
            val raceRecord2 = RaceRecord(0, "FERRARI", 1)
            val raceRecord3 = RaceRecord(1, "FERRARI", 1)

            return Stream.of(
                Arguments.of(raceRecord1, raceRecord2, listOf(raceRecord1)), // 1등이 1명
                Arguments.of(raceRecord1, raceRecord3, listOf(raceRecord1, raceRecord3)) // 1등이 2명
            )
        }
    }

    @DisplayName("현재 레코드에서의 선두를 찾는다.")
    @ParameterizedTest
    @MethodSource("findLeaderTestProvider")
    fun findLeaderTest(raceRecord1: RaceRecord, raceRecord2: RaceRecord, resultRecords: List<RaceRecord>) {
        //given
        val raceRecords = RaceRecords(listOf(raceRecord1, raceRecord2))

        //when
        val raceRecord = raceRecords.findLeader()

        //then
        assertThat(raceRecord).isEqualTo(resultRecords)
    }

    @DisplayName("경기 기록을 시간 오름차순으로 정렬하여 반환한다.")
    @Test
    fun getRecordsOrderByTimeAscTest() {
        //given
        val ford = RacingCar(name = "FORD", engine = { true })
        val goAndStop: Queue<Boolean> = LinkedList()
        goAndStop.add(true)
        goAndStop.add(false)
        val ferarri = RacingCar(name = "FERRARI", engine = { goAndStop.poll() })

        val race = Race.of(listOf(ford, ferarri), 2)
        race.start()

        //when
        val recordsOrderByTimeAsc = race.getRecordsOrderByTimeAsc()

        //then
        val firstRecords = recordsOrderByTimeAsc[0]
        assertThat(firstRecords.getTime()).isEqualTo(1)
        assertThat(firstRecords.raceRecords).filteredOn("name", "FORD").extracting("distance").contains(1)

        val lastRecords = recordsOrderByTimeAsc[1]
        assertThat(lastRecords.raceRecords).filteredOn("name", "FORD").extracting("distance").contains(2)
        assertThat(lastRecords.findLeader()).hasSize(1)
        assertThat(lastRecords.findLeader()[0].name).isEqualTo("FORD")
    }
}
