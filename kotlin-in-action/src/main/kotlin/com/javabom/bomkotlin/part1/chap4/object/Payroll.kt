package com.javabom.bomkotlin.part1.chap4.`object`

//싱글턴
object Payroll {
    val allEmployees = arrayListOf<Person>()

    fun calculateSalary() {
        for (person in allEmployees) {

        }
    }
}

class Person(
    val name: String,
    val salary: Long,
) {

    object NameComparator : Comparator<Person> {
        override fun compare(o1: Person, o2: Person): Int {
            return o1.name.compareTo(o2.name)
        }
    }

}
