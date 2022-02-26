package bom.javabom.effectivekotlin.chap2

class CapturingSample {

    fun runLikeJava() {
        var numbers = (2..100).toList()
        val primes = mutableListOf<Int>()
        while (numbers.isNotEmpty()) {
            val prime = numbers.first()
            primes.add(prime)
            numbers = numbers.filter { it % prime != 0 }
        }
        println(primes)
    }

    fun runWithSequenceBuilder() {
        val primes: Sequence<Int> = sequence {
            var numbers = generateSequence(2) { it + 1 }

            while (true) {
                val prime = numbers.first()
                yield(prime)
                numbers = numbers.drop(1)
                    .filter { it % prime != 0 }
            }
        }
        println(primes.take(25).toList())
    }

    fun runWithSequenceBuilderButWrongScope() {
        val primes: Sequence<Int> = sequence {
            var numbers = generateSequence(2) { it + 1 }
            var prime: Int
            while (true) {
                prime = numbers.first()
                yield(prime)
                val filterNumbers = numbers.drop(1)
                    .filter { it % prime != 0 }
                numbers = filterNumbers
            }
        }
        println(primes.take(25).toList())
    }


}

fun main() {
    val sample = CapturingSample()
    sample.runLikeJava()
    sample.runWithSequenceBuilder()
    sample.runWithSequenceBuilderButWrongScope()
}

