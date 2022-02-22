package bom.javabom.effectivekotlin.chap2

import java.lang.IllegalStateException

class CapturingSample {

    fun run1() {
        var numbers = (2..100).toList()
        val primes = mutableListOf<Int>()
        while (numbers.isNotEmpty()) {
            val prime = numbers.first()
            primes.add(prime)
            numbers = numbers.filter { it % prime != 0 }
        }
        println(primes)
    }

    fun run2() {
        val primes: Sequence<Int> = sequence {
            println("sequence")
            var numbers = generateSequence(2) { it + 1 }

            while (true) {
                val prime = numbers.first()
                yield(prime)
                numbers = numbers.drop(1)
                    .filter {
                        it % prime != 0
                    }
            }
        }
        println(primes.take(25).toList())
    }

    fun run3() {
        val primes: Sequence<Int> = sequence {
            var numbers = generateSequence(2) { it + 1 }
            var prime: Int
            while (true) {
                prime = numbers.first()
                println("prime : $prime")
                println("after: ${numbers.asIterable().take(10).toList()}\n")
                yield(prime)
                numbers = numbers.drop(1)
                    .filter { it % prime != 0 }
            }
        }
        println(primes.take(10).toList())
    }

}

fun main() {
    val sample = CapturingSample()
//    sample.run1()
//    sample.run2()
    sample.run3()
}

