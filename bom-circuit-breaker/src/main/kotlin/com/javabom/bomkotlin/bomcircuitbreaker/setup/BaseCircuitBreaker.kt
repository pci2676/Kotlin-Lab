package com.javabom.bomkotlin.bomcircuitbreaker.setup

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.decorators.Decorators

class BaseCircuitBreaker(
    private val delegate: CircuitBreaker,
    fallbackThrowableCollection: Collection<Class<out Throwable>> = emptyList(),
) {
    private val fallbackThrowableCollection =
        listOf(CallNotPermittedException::class.java, *fallbackThrowableCollection.toTypedArray())

    fun <T> execute(
        method: () -> T,
        fallbackMethod: ((e: Throwable) -> T)? = null,
    ): T {
        val decorators = Decorators.ofCallable(method)
            .withCircuitBreaker(delegate)

        fallbackMethod?.run {
            decorators.withFallback(fallbackThrowableCollection, this)
        }

        return decorators.decorate()
            .call()
    }

    fun status(): CircuitBreakerStatus {
        @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
        return when (delegate.state) {
            CircuitBreaker.State.DISABLED,
            CircuitBreaker.State.METRICS_ONLY,
            CircuitBreaker.State.HALF_OPEN,
            CircuitBreaker.State.CLOSED,
            -> CircuitBreakerStatus.CLOSE

            CircuitBreaker.State.FORCED_OPEN,
            CircuitBreaker.State.OPEN,
            -> CircuitBreakerStatus.OPEN
        }
    }
}
