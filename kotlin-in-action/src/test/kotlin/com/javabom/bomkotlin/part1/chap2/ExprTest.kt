package com.javabom.bomkotlin.part1.chap2

import com.javabom.bomkotlin.part1.chap2.Expr.Num
import com.javabom.bomkotlin.part1.chap2.Expr.Sum
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ExprTest {

    @DisplayName("노드를 통해 더하기")
    @Test
    fun exprTest1() {
        val sum = eval(Sum(Sum(Num(1), Num(2)), Num(4)))

        assertThat(sum).isEqualTo(7)
    }

}
