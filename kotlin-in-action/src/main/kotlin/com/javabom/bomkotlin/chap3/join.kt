package com.javabom.bomkotlin.chap3

import java.lang.StringBuilder

var opCount = 0

private fun performOperation() {
    opCount++
}

fun reportOperationCount() {
    println("Operation performed $opCount times")
}

private const val DEFAULT_SEPARATOR = ", "
private const val DEFAULT_PREFIX = ""
private const val DEFAULT_POSTFIX = ""

fun <T> joinToString(
    collection: Collection<T>,
    separator: String = DEFAULT_SEPARATOR,
    prefix: String = DEFAULT_PREFIX,
    postfix: String = DEFAULT_POSTFIX,
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    performOperation()
    return readLine().toString()
}
