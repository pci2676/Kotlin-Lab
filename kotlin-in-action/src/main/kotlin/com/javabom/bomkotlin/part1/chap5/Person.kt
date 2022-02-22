package com.javabom.bomkotlin.part1.chap5

data class Person(
    val name: String,
    val age: Int,
)

fun Iterable<Person>.findTheOldest(): Person? {
    var maxAge = 0
    var theOldest: Person? = null

    for (person in this) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    return theOldest
}

fun findTheOldest(people: List<Person>): Person? {
    var maxAge = 0
    var theOldest: Person? = null

    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    return theOldest
}
