package com.javabom.bomkotlin.chap3

// 확장 함수는 오버라이드할 수 없다.
fun <T> Collection<T>.joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return readLine().toString()
}
