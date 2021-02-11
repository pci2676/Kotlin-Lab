package com.javabom.bomkotlin.chap3

// String: 수신객체타입         this: 수신객체
// = fun String.lastOf(): Char = get(length - 1)  // 수신객체멤버에는 this 없이 접근 할 수 있다.
fun String.lastOf(): Char = this[this.length - 1]

// 확장 프로퍼티
var StringBuilder.lastChar: Char
    get() =
        get(length - 1)
    set(value) {
        this.setCharAt(this.length - 1, value)
    }
