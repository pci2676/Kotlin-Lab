package com.javabom.bomkotlin.chap5

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PersonTest {
    @DisplayName("가장 나이든 사람 찾기")
    @Test
    fun `가장 나이든 사람 찾기`() {
        //given
        val people = listOf(Person("a", 1), Person("b", 2), Person("c", 3))

        //when
        val oldestAge = people.maxOf { it.age }
        val theOldest = people.findTheOldest()!!

        //then
        assertThat(oldestAge).isEqualTo(theOldest.age)
    }
}
