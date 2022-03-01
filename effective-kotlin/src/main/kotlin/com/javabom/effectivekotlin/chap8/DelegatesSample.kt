package com.javabom.effectivekotlin.chap8

import java.util.UUID
import kotlin.reflect.KProperty

class IdDelegate(
    var id: String = UUID.randomUUID().toString(),
) {
    operator fun getValue(self: Any, property: KProperty<*>): String {
        return this.id
    }

    operator fun setValue(self: Any, property: KProperty<*>, newValue: String) {
        this.id = newValue
    }
}

class Product {
    val productId: String by IdDelegate()
}
