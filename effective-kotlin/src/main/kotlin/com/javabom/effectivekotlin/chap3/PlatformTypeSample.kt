package com.javabom.effectivekotlin.chap3

fun statedType() {
    val value: String = JavaString().value // 플랫폼 타입을 Kotlin의 String으로 받아들이는 시점에서

    println(value.length)
}

fun platformType() {
    val value = JavaString().value

    println(value.length) // 플랫폼 타입을 직접 활용하는 시점에서
}

fun main() {
    statedType()
    platformType()
}
