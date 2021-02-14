package com.javabom.bomkotlin.chap5

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.StringBuilder

class WithAndApplyTest {
    @DisplayName("with 없이 알파벳 만들기")
    @Test
    fun `with 함수 사용해보기`() {
        //given
        val alphabetFun = fun(): String {
            val result = StringBuilder()
            for (letter in 'A'..'Z') {
                result.append(letter)
            }
            return result.toString()
        }

        //when
        //then
        print(run(alphabetFun))
    }

    @DisplayName("with 으로 알파벳 만들기")
    @Test
    fun `with 으로 알파벳 만들기`() {
        //given
        val alphabetFun = fun(): String = with(StringBuilder()) {
            for (letter in 'A'..'Z') {
                append(letter) // 행위의 기반은 수신 객체(=this)이다.
            }
            toString() // 절차적으로 수신객체가 행하도록 한다.
        }

        //when
        //then
        println(run(alphabetFun))
    }

    @DisplayName("apply 로 알파벳 만들기")
    @Test
    fun `apply 로 알파벳 만들기`() {
        //given
        val alphabets = StringBuilder().apply { // apply는 수신객체를 반환한다.
            for (letter in 'A'..'Z') {
                append(letter)
            }
        }.toString()

        //when
        //then
        println(alphabets)
    }

    @DisplayName("buildString 이 짱짱")
    @Test
    fun `buildString 이 짱짱`() {
        //given
        val alphabets = buildString {
            for (letter in 'A'..'Z') {
                append(letter)
            }
        }

        //when
        //then
        println(alphabets)
    }
}
