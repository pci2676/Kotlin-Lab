package com.javabom.bomkotlin.part2.chap7

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PointTest {

    @DisplayName("더하기 연산자 오버로딩 예제")
    @Test
    fun plusTest() {
        //given
        val sum = Point(1, 2) + Point(2, 3)

        //when

        //then
        assertThat(sum).isEqualTo(Point(3, 5))
    }
}
