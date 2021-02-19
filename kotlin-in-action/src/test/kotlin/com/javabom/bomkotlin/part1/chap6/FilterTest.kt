package com.javabom.bomkotlin.part1.chap6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FilterTest {
    @DisplayName("필터테스트")
    @Test
    fun `필터테스트`() {
        //given
        val list = listOf<Int?>(1, null, 3, null, 5)

        //when
        val notNullList = list.filterNotNull()

        //then
        assertThat(notNullList.sum()).isEqualTo(9)
    }

    @DisplayName("arrayOfNull 은 null 로 된 배열을 만든다. 어디다 쓰려고 만든거지?")
    @Test
    fun `arrayOfNullTest`() {
        //given
        val arrayOfNulls = arrayOfNulls<Int>(3)

        //when

        //then
        assertThat(arrayOfNulls).hasSize(3)
        assertThat(arrayOfNulls).containsOnlyNulls()
    }

    @DisplayName("알파벳으로 이뤄진 배열 만들기")
    @Test
    fun `알파벳으로 이뤄진 배열 만들기`() {
        //given
        val alphabets = Array(26) { i -> ('a' + i).toString() }

        //when
        alphabets.forEach { print(it) }

        //then
        assertThat(alphabets).hasSize(26)
    }

    @DisplayName("숫자채우기")
    @Test
    fun `숫자채우기`() {
        //given
        val array = Array(26) { i -> i + 1 }

        //when
        array.forEach { println(it) }

        //then
        assertThat(array).contains(1, 26)
    }

    @DisplayName("동적인자를 넘기려면 스프레드 연산자 [*] 사용")
    @Test
    fun `dynamicTest`() {
        //given
        val list = listOf(1, 2, 3)
        //when

        //then
        println("%d %d %d".format(*list.toTypedArray()))
    }
}
