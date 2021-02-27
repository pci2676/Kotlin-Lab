package com.javabom.bomkotlin.part2.chap7.destructuring

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ComponentTest {
    @Test
    fun `구조분해 테스트`() {
        //given
        val component = Component(1, 2)

        //when
        val (x, y, z) = component

        //then
        assertThat(x).isEqualTo(1)
        assertThat(y).isEqualTo(2)
        assertThat(z).isEqualTo(3)
    }
}
