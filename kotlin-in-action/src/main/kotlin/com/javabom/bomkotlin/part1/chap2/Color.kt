package com.javabom.bomkotlin.part1.chap2

enum class Color(
    val r: Int,
    val g: Int,
    val b: Int,
) {
    RED(255, 0, 0),
    ORANGE(255, 165, 0),
    YELLOW(255, 255, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    INDIGO(75, 0, 130),
    VIOLET(238, 130, 238),
    ;

    fun rgb() = (r * 256 + g) * 256 + b

    fun getMnemonic(color: Color) = when (color) {
        RED -> "Richard"
        ORANGE -> "Of"
        YELLOW -> "York"
        GREEN -> "Gave"
        BLUE -> "Battle"
        INDIGO -> "In"
        VIOLET -> "Vain"
    }

    fun getWarmth(color: Color) = when (color) {
        RED, ORANGE, YELLOW -> "warm"
        GREEN -> "neutral"
        BLUE, INDIGO, VIOLET -> "cold"
    }

    fun mix(color1: Color, color2: Color) = when (setOf(color1, color2)) {
        setOf(RED, YELLOW) -> ORANGE
        setOf(YELLOW, BLUE) -> GREEN
        setOf(BLUE, VIOLET) -> INDIGO
        else -> throw RuntimeException("Dirty Color")
    }

    fun mixOptimized(color1: Color, color2: Color) = when {
        (color1 == RED && color2 == YELLOW) || (color1 == YELLOW && color2 == RED) -> ORANGE
        (color1 == BLUE && color2 == YELLOW) || (color1 == YELLOW && color2 == BLUE) -> GREEN
        (color1 == BLUE && color2 == VIOLET) || (color1 == VIOLET && color2 == BLUE) -> INDIGO
        else -> throw RuntimeException("Dirty Color")
    }
}
