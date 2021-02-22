package com.javabom.bomkotlin.part2.chap7.lotto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * 복합연산자랑 연산자 오버로딩을 동시에하지말자
 * 변경가능한 클래스를 설계할때는 복합연산자를 정의하자
 */
internal class LottoTest {
    @DisplayName("복합 대입 연산자 오버로딩")
    @Test
    fun `복합 대입 연산자 오버로딩`() {
        //given
        val lottoNumbers = mutableListOf<LottoNumber>()

        //when
        (1..6).forEach {
            lottoNumbers += LottoNumber(it)
        }
        val lottoTicket = LottoTicket(lottoNumbers)

        //then
        assertThat(lottoTicket.numbers).hasSize(6)
    }
}
