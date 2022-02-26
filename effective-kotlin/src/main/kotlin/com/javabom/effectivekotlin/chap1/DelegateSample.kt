package com.javabom.effectivekotlin.chap1

interface Movable {
    fun move()
}

class Car : Movable {
    override fun move() {
        println("move")
    }
}

class KotlinCar(car: Car) : Movable by car

class KotlinOverrideCar(val car: Car) : Movable by car {
    override fun move() {
        println("m o v e")
    }
}

////// 자바였다면...
class JavaCar(val car: Car) : Movable {
    override fun move() {
        car.move()
    }
}

fun main() {
    val delegate = Car()
    KotlinCar(delegate).move()
    KotlinOverrideCar(delegate).move()
    JavaCar(delegate).move()
}
