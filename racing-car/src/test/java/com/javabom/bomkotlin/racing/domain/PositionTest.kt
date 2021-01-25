package com.javabom.bomkotlin.racing.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PositionTest {

    @DisplayName("1 전진한다.")
    @Test
    internal fun forwardTest() {
        //given
        val position = Position.zero()

        //when
        val forward = position.forward()

        //then
        assertThat(forward.distance).isEqualTo(1)
    }

    @DisplayName("거리가 0인 객체생성")
    @Test
    internal fun zeroTest() {
        assertThat(Position.zero()).isEqualTo(Position(0))
    }
}
