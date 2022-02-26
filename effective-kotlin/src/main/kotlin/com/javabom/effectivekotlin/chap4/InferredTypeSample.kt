package com.javabom.effectivekotlin.chap4

open class Car
class Ferrari : Car()

val DEFAULT_CAR: Car = Ferrari()

interface Library {
    fun produce(): Car = DEFAULT_CAR
}

fun main() {
    val library = object : Library {
        override fun produce(): Car {
            return super.produce()
        }
    }

    val car: Car = library.produce()
}
