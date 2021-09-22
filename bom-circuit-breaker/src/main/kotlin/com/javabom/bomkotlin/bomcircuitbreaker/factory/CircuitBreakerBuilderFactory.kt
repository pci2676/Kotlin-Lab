package com.javabom.bomkotlin.bomcircuitbreaker.factory

import com.javabom.bomkotlin.bomcircuitbreaker.setup.CircuitBreakerProperties
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig

object CircuitBreakerBuilderFactory {

    /**
     * 서킷 브레이커 Builder
     */
    fun circuitBreakerBuilder(
        circuitBreakerProperties: CircuitBreakerProperties,
        ignoreExceptions: List<Class<out Throwable>>? = null,
    ): CircuitBreaker {
        val configBuilder = CircuitBreakerConfig.custom()
            .failureRateThreshold(circuitBreakerProperties.failureRate())
            .slowCallRateThreshold(circuitBreakerProperties.slowCallRate())
            .slowCallDurationThreshold(circuitBreakerProperties.slowCallDuration())
            .slidingWindowType(circuitBreakerProperties.slidingWindowType())
            .slidingWindowSize(circuitBreakerProperties.slidingWindowSize!!)
            .minimumNumberOfCalls(circuitBreakerProperties.minimumNumberOfCalls!!)
            .waitDurationInOpenState(circuitBreakerProperties.waitDurationInOpenState())
            .permittedNumberOfCallsInHalfOpenState(circuitBreakerProperties.permittedNumberOfCallsInHalfOpenState!!)
            .automaticTransitionFromOpenToHalfOpenEnabled(circuitBreakerProperties.automaticTransition)

        ignoreExceptions?.run {
            configBuilder.ignoreExceptions(*this.toTypedArray())
        }

        val config = configBuilder.build()

        return CircuitBreaker.of(circuitBreakerProperties.name!!, config)
    }
}
