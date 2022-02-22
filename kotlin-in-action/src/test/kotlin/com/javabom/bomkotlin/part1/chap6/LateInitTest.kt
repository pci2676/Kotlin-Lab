package com.javabom.bomkotlin.part1.chap6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LateInitTest {
    /**
     *  초기화하지 않았지만 null 인 상태로 쓸수없게 설정한 상황
     *  lateinit 대상은 따라서 var 여야만 한다.
     */
    private lateinit var myService: MyService

    @DisplayName("lateInit 사용해서 나중에 초기화하기")
    @Test
    fun lateInitTest() {
        //given
        myService = MyService() // null 이 아니도록 설정

        //when

        //then
        assertThat(myService.performAction()).isEqualTo("hello")
    }

    @DisplayName("lateinit 을 초기화 하지 않으면 exception")
    @Test
    fun `lateinit 을 초기화 하지 않으면 exception`() {
        //given

        //when

        //then
        val exceptionMessage = assertThrows<UninitializedPropertyAccessException> {
            myService.performAction()
        }.message
        assertThat(exceptionMessage).isEqualTo("lateinit property myService has not been initialized")
    }

    internal class MyService {
        fun performAction(): String = "hello"
    }
}
