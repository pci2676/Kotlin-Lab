package com.javabom.bomkotlin.chap6

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GenericTypeNullableTest {
    @DisplayName("타입 상한선을 넣지 않으면 null 주입 가능")
    @Test
    fun `nullable 테스트`() {
        val myService = MyService()

        myService.nullableFunction(null)
        //아래는 불가능
//        myService.nonNullableFunction(null)
//        myService.nonNullableFunction(myService.getNullableString(true))
        assertThrows<NullPointerException> {
            myService.nonNullableFunction(myService.getNullableString(true)!!)
        }
        myService.nonNullableFunction(myService.getNullableString(false)!!)
    }
}

internal class MyService {
    fun <T> nullableFunction(t: T): T {
        return t
    }

    fun <T : Any> nonNullableFunction(t: T): T {
        return t
    }

    fun getNullableString(nullable: Boolean): String? {
        return if (nullable) {
            null
        } else {
            "value"
        }
    }
}
