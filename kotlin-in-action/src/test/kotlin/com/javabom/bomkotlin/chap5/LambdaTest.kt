package com.javabom.bomkotlin.chap5

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class LambdaTest {
    @DisplayName("람다를 선언해서 사용하기")
    @Test
    fun `람다를 선언해서 사용하기`() {
        //given
        val nextAction = ::sendMail

        val action = { person: Person, message: String ->
            {
                nextAction(person, message)
            }
        }

        //when
        //then
        action(Person("a", 1), "메세지").invoke()

        val provider = ::Person // 생성자를 펑션으로 꺼내기
        run(action(provider("b", 2), "메세지2"))
    }

    @DisplayName("시퀀스로 레이지 연산")
    @Test
    fun `시퀀스로 레이지 연산`() {
        //given
        val list = mutableListOf<Int>()
        for (i in (1..1000000)) {
            list.add(i)
        }

        //when
        //then
        val eager = list.asSequence() // asSequence 를 안붙이면 계속 체이닝 마다 리스트 생성해서 비용이 비싸다
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .toList()
    }
}

fun sendMail(person: Person, message: String) {
    println("$person : $message")
}
