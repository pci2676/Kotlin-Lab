package com.javabom.bomkotlin.chap3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ButtonTest {

    @DisplayName("멤버함수는 오버라이드 가능하다.")
    @Test
    fun clickOverrideTest() {
        val button: View = Button()

        val clickValue = button.clickValue()

        assertThat(clickValue).isEqualTo(2)
    }

    @DisplayName("확장함수는 오버라이드 불가능하다.")
    @Test
    fun showOverrideExceptionTest() {
        val button: View = Button()

        val value = button.getValue()

        assertThat(value).isEqualTo(1)
    }
}
