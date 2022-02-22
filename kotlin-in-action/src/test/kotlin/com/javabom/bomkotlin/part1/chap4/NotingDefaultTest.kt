package com.javabom.bomkotlin.part1.chap4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class NotingDefaultTest {
    @DisplayName("생성자를 정의해두지 않아도 기본 생성자는 제공한다.")
    @Test
    fun test1() {
        assertThat(NotingDefault()).isNotNull
    }

}
