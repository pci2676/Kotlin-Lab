package com.javabom.bomkotlin.part1.chap4.decorator

class CountingSet<T>(
    val innerSet: MutableSet<T> = HashSet()
) : MutableCollection<T> by innerSet {
    var objectAdded = 0

    override fun add(element: T): Boolean {
        objectAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectAdded += elements.size
        return innerSet.addAll(elements)
    }
}
