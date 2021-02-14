package com.javabom.bomkotlin.chap4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class DefaultTest {

    @DisplayName("기본 값을 설정해 두면 기본 생성자를 제공한다.")
    @Test
    fun getName() {
        //given
        val default = Default()
        val input = Default("input")

        //when

        //then
        assertThat(default.name).isEqualTo("default")
        assertThat(input.name).isEqualTo("input")
    }
}
