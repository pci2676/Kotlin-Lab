package com.javabom.bomkotlin.racing.domain

import com.javabom.bomkotlin.racing.domain.creator.RacingCarCreator
import com.javabom.bomkotlin.racing.domain.racingcar.RacingCar
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class RacingCarCreatorTest {

    @DisplayName("이름을 RacingCar로 변경한다.")
    @Test
    fun toRacingCarTest() {
        //given
        val racingCarCreator = RacingCarCreator(arrayListOf("FORD"))

        //when
        val racingCar = racingCarCreator.toRacingCar { true }[0]

        //then
        assertThat(racingCar).isEqualTo(RacingCar("FORD") { true })
    }

    @DisplayName("빈 이름 리스트로 객체를 생성할 수 없다.")
    @Test
    internal fun validateTest() {
        assertThatThrownBy { RacingCarCreator(emptyList()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("비어있는 이름이 있습니다.")
    }
}
