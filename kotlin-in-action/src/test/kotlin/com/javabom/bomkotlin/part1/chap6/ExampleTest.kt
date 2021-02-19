package com.javabom.bomkotlin.part1.chap6

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ExampleTest {

    @DisplayName("안전한 호출 연산자 [?.] 를 이용하면 null 을 반환하게 할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = [",true", "hello,false"])
    fun `안전한 호출 연산자 테스트 1`(input: String?, expect: Boolean) {
        //given

        //when
        val result = input?.toUpperCase()

        //then
        assertThat(result.isNullOrBlank()).isEqualTo(expect)
    }

    @DisplayName("엘비스연산자 [?:] 는 기본값을 지정해줄 수 있다.")
    @ParameterizedTest
    @CsvSource(value = [",default", "hello,HELLO"])
    fun `엘비스연산자는 기본값을 지정해줄 수 있다`(input: String?, expect: String) {
        //given

        //when
        val result = input?.toUpperCase() ?: "default"

        //then
        assertThat(result).isEqualTo(expect)
    }

    @DisplayName("as 로 안전한 캐스트가 가능하다.")
    @ParameterizedTest
    @CsvSource(value = [",true", "hello,false"])
    fun `asTest`(input: Any?, expect: Boolean) {
        //given

        //when
        val result = input as? String

        //then
        assertThat(result.isNullOrBlank()).isEqualTo(expect)
    }

    @DisplayName("null 아님 단언 !!")
    @ParameterizedTest
    @CsvSource(value = [",true", "hello,false"])
    fun `nullTest`(input: String?, isNull: Boolean) {
        if (isNull) {
            assertThatThrownBy { input!! }
                .isInstanceOf(NullPointerException::class.java)
        }
    }

    @DisplayName("let 은 단일 map 이라고 생각하자.")
    @ParameterizedTest
    @CsvSource(value = [",true,", "hello,false,true"])
    fun `let 사용하기`(input: String?, expect: Boolean, expect2: Boolean?) {
        //given
        val set = mutableSetOf<String>()

        //when
        val result = input?.let {
            set.add(it)
        }

        //then
        assertThat(set.isEmpty()).isEqualTo(expect)
        assertThat(result).isEqualTo(expect2)
    }
}
