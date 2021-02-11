package com.javabom.bomkotlin.chap2

import com.javabom.bomkotlin.chap2.Expr.Num
import com.javabom.bomkotlin.chap2.Expr.Sum

interface Expr {
    class Num(val value: Int) : Expr // value 라는 프로퍼티가 존재하는 Expr 구현체
    class Sum(val left: Expr, val right: Expr) : Expr // Num, Sum 을 인자로 받는 Expr 구현체
}

// is : 스마트 캐스팅으로 이후에는 캐스팅한 것처럼 행동가능
fun eval(expr: Expr): Int = when (expr) {
    is Num -> expr.value
    is Sum -> eval(expr.right) + eval(expr.left)
    else -> throw IllegalArgumentException("Unknown Expression")
}

