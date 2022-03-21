package com.javabom.effectivekotlin.chap15.tobe

class Node(val name: String) {
    fun makeChild(childName: String) =
        create("$name.$childName")
            .also { // it: Node?
                print("Create ${it?.name}")
            }

    fun create(name: String): Node? = Node(name)
}

class LabelNode(val name: String) { // this@LabelNode
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply { //this: Node?
                print("Create ${this@LabelNode.name}")
            }

    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
