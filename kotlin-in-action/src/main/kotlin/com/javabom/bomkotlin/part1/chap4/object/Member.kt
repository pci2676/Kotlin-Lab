package com.javabom.bomkotlin.part1.chap4.`object`

class Member private constructor(val name: String) {
    companion object Loader {
        fun of(name: String): Member = Member(name)
    }
}

interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Member2(val name: String) {
    companion object : JSONFactory<Member2> {
        override fun fromJSON(jsonText: String): Member2 {
            return Member2(jsonText)
        }
    }
}
