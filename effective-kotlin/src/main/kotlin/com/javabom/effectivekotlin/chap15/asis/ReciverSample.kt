package com.javabom.effectivekotlin.chap15.asis

class Node(val name: String) {
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply {
                print("Create $name")
            }

    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
