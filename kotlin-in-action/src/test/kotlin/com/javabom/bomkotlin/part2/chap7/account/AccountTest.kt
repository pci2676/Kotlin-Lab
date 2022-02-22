package com.javabom.bomkotlin.part2.chap7.account

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class AccountTest {
    @DisplayName("연산자 오버로딩 테스트")
    @Test
    fun `연산자 오버로딩 테스트`() {
        //given
        val account = Account()
        val setting = Setting(true)

        //when
        val changedAccount = account + setting

        //then
        assertThat(changedAccount.setting!!.enable).isTrue
    }


}
