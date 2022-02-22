package com.javabom.bomkotlin.bomcircuitbreaker

import com.javabom.bomkotlin.bomcircuitbreaker.factory.CircuitBreakerFactory
import com.javabom.bomkotlin.bomcircuitbreaker.setup.CircuitBreakerProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CircuitBreakerRegistryCreateTest {
    private val defaultProperties = CircuitBreakerProperties(
        name = "testCircuitBreaker",
        failureRate = 50, // 50%
        slowCallRate = 10,
        slowCallDuration = 3,
        slidingWindowType = "COUNT_BASED",
        slidingWindowSize = 10, // 최근 시도 10개만 쌓아둠
        permittedNumberOfCallsInHalfOpenState = 10,
        minimumNumberOfCalls = 2, // 2개씩 실패율 측정
        waitDurationInOpenState = 3,
        automaticTransition = false
    )

    @Test
    fun `개별생성 테스트`() {
        //given
        val circuitBreaker1 = CircuitBreakerFactory.circuitBreaker(defaultProperties)
        val circuitBreaker2 = CircuitBreakerFactory.circuitBreaker(defaultProperties)

        //when

        //then
        assertThat(circuitBreaker1).isNotEqualTo(circuitBreaker2)
    }

    @Test
    fun `등록된 registry 를 통한 생성 테스트`() {
        //given
        val circuitBreaker1 = CircuitBreakerFactory.computeIfAbsent(
            circuitBreakerName = "name1",
            circuitBreakerProperties = defaultProperties,
            ignoreExceptions = listOf()
        )
        val circuitBreaker2 = CircuitBreakerFactory.computeIfAbsent(
            circuitBreakerName = "name1",
            circuitBreakerProperties = defaultProperties,
            ignoreExceptions = listOf()
        )

        // 한번 생성되면 이후 properties 없이 name으로 생성된 객체 불러온다.
        val circuitBreaker3 = CircuitBreakerFactory.computeIfAbsent(
            circuitBreakerName = "name1",
            circuitBreakerProperties = null,
            ignoreExceptions = listOf(),
        )

        //when

        //then
        assertThat(circuitBreaker1).isEqualTo(circuitBreaker2)
        assertThat(circuitBreaker1).isEqualTo(circuitBreaker3)
    }
}
