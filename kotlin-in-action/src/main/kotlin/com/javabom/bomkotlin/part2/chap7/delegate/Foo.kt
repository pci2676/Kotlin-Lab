package com.javabom.bomkotlin.part2.chap7.delegate

import kotlin.reflect.KProperty

class Foo {
    var number: Number by Delegate()
}

class Number(val value: Int)

class Delegate {
    operator fun getValue(foo: Foo, property: KProperty<*>): Number {
        println("delegate")
        return foo.number
    }

    operator fun setValue(foo: Foo, property: KProperty<*>, number: Number) {
        println("delegate")
        foo.number = number
    }
}
