package com.javabom.bomkotlin.part2.chap7.destructuring

class Component(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = x + y
}
