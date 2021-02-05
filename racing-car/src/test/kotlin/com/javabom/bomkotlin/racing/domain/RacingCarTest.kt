package com.javabom.bomkotlin.racing.domain

import com.javabom.bomkotlin.racing.domain.racingcar.RacingCar
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class RacingCarTest {

    @DisplayName("engine 이 true 면 전진 아니면 제자리에 있는다.")
    @ParameterizedTest
    @CsvSource("true,1", "false,0")
    fun moveTest(power: Boolean, distance: Int) {
        //given
        var car = RacingCar(name = "박찬인") { power }

        //when
        car = car.move()

        //then
        assertThat(car.getDistance()).isEqualTo(distance)
    }
}
