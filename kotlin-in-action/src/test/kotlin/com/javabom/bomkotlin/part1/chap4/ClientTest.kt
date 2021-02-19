package com.javabom.bomkotlin.part1.chap4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ClientTest {

    @DisplayName("== 이 내부적으로 equals 를 부른다.")
    @Test
    fun testEquals() {
        //given
        val client1 = Client("pci", 1)
        val client2 = Client("pci", 1)

        //when
        val result = client1 == client2

        //then
        assertThat(result).isTrue
    }

    @DisplayName("===로 참조비교 한다.")
    @Test
    fun testEquals2() {
        //given
        val client1 = Client("pci", 1)
        val client2 = Client("pci", 1)

        //when
        val result = client1 === client2

        //then
        assertThat(result).isFalse
    }
}
