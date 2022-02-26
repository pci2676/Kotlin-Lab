package bom.javabom.effectivekotlin.chap2

fun getColor(colorName: String): Int {
    val colorNumber = when (colorName) {
        "RED" -> 1
        "BLUE" -> 2
        else -> 3
    }
    return colorNumber
}

fun printWeather(degrees: Int) {
    val (description, color) = when {
        degrees < 5 -> "cold" to 2
        degrees < 23 -> "mild" to 3
        else -> "hot" to 1
    }

    println("weather is $description($degrees)")
}
