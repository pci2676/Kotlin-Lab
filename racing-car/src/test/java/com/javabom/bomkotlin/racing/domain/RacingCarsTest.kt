package com.javabom.bomkotlin.racing.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class RacingCarsTest {

    @DisplayName("전체 차량을 움직인다.")
    @Test
    internal fun moveTest() {
        //given
        val ford = RacingCar("FORD") { true }
        val ferrari = RacingCar("FERRARI") { false }
        val racingCars = RacingCars(arrayListOf(ford, ferrari))

        //when
        racingCars.move()

        //then
        assertThat(ford.getDistance()).isEqualTo(1)
        assertThat(ferrari.getDistance()).isEqualTo(0)
    }

    @DisplayName("현재 차량들의 기록을 반환한다.")
    @Test
    fun getRecordTest() {
        //given
        val ford = RacingCar("FORD") { true }
        val ferrari = RacingCar("FERRARI") { false }
        val racingCars = RacingCars(arrayListOf(ford, ferrari))

        //when
        val records = racingCars.getRecords(1)

        //then
        assertThat(records).hasSize(2)
    }
}
