package com.javabom.bomkotlin.bomcircuitbreaker.factory

import com.javabom.bomkotlin.bomcircuitbreaker.common.logger
import com.javabom.bomkotlin.bomcircuitbreaker.setup.CircuitBreakerProperties
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry

object CircuitBreakerFactory {
    private val log = logger()

    private val circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()

    fun circuitBreaker(
        circuitBreakerProperties: CircuitBreakerProperties,
        ignoreExceptions: List<Class<out Throwable>>? = null,
    ): CircuitBreaker {
        val configBuilder = createConfigBuilder(circuitBreakerProperties, ignoreExceptions)

        val config = configBuilder.build()

        val circuitBreaker = CircuitBreaker.of(circuitBreakerProperties.name!!, config)

        circuitBreaker.eventPublisher.onStateTransition { log.error { it.toString() } }

        return circuitBreaker
    }

    private fun createConfigBuilder(
        circuitBreakerProperties: CircuitBreakerProperties,
        ignoreExceptions: List<Class<out Throwable>>? = null,
    ): CircuitBreakerConfig.Builder {
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

        return configBuilder
    }

    fun computeIfAbsent(
        circuitBreakerName: String,
        circuitBreakerProperties: CircuitBreakerProperties? = null,
        ignoreExceptions: List<Class<out Throwable>>? = null,
        tags: Map<String, String> = emptyMap(),
    ): CircuitBreaker {
        val config = circuitBreakerProperties?.let { createConfigBuilder(it, ignoreExceptions).build() }
            ?: circuitBreakerRegistry.defaultConfig

        return circuitBreakerRegistry.circuitBreaker(circuitBreakerName, config, io.vavr.collection.HashMap.ofAll(tags))
    }
}
