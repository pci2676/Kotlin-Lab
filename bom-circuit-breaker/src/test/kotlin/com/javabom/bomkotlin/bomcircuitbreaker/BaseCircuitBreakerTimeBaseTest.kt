package com.javabom.bomkotlin.bomcircuitbreaker

import com.javabom.bomkotlin.bomcircuitbreaker.factory.CircuitBreakerBuilderFactory.circuitBreakerBuilder
import com.javabom.bomkotlin.bomcircuitbreaker.setup.BaseCircuitBreaker
import com.javabom.bomkotlin.bomcircuitbreaker.setup.CircuitBreakerProperties
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

@Suppress("NonAsciiCharacters")
internal class BaseCircuitBreakerTimeBaseTest {

    @TestFactory
    fun `서킷브레이커의 실패율 계산 최소 단위만큼 실행하지 않으면 실패율 계산을 하지 않는다`(): Stream<DynamicTest> {
        //given
        val failCountPer = 10 // 10개마다 측정
        val windowSize = 3 // 통계는 최근 3초간만 집계

        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 100, // 100% 전부 실패해야지 서킷브레이커를 open 한다.
            slowCallRate = 10,
            slowCallDuration = 3,
            slidingWindowType = SlidingWindowType.TIME_BASED.name,
            slidingWindowSize = windowSize,
            minimumNumberOfCalls = failCountPer,
            permittedNumberOfCallsInHalfOpenState = 10,
            waitDurationInOpenState = 3,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties)

        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("10번의 시도중 9개의 시도가 실패해도 실패율 계산은 되어있지 않다.") {
                //when
                repeat(9) {
                    runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }
                }
                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(-1.0f)
            },
            dynamicTest("10번의 시도중 마지막 시도가 실행되면 계산을 한다.") {
                //when
                circuitBreaker.execute(method = {})

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(90f)
            },
            dynamicTest("최소 측정단위가 넘어간 시점부터 통계를 갱신한다.") {
                //when
                circuitBreaker.execute(method = {})

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo((9f / 11f) * 100f)
            },
        )
    }

    @TestFactory
    fun `TIME_BASE 서킷브레이커 OPEN to CLOSE 테스트`(): Stream<DynamicTest> {
        //given
        val openWaitMills = 1

        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 60, // 60%
            slowCallRate = 10,
            slowCallDuration = 3,
            slidingWindowType = SlidingWindowType.TIME_BASED.name,
            slidingWindowSize = 3, // 최근 3초만 쌓아둠
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = 10, // 10개씩 실패율 측정
            waitDurationInOpenState = openWaitMills,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties)
        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("서킷브레이커 OPEN") {
                repeat(6) {
                    runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }
                }
                repeat(4) {
                    runCatching { circuitBreaker.execute(method = { }) }
                }

                assertThat(delegate.metrics.failureRate).isEqualTo(60f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.OPEN)
            },
            dynamicTest("waitDurationInOpenState 시간이 흐른뒤 요청이 오면 HALF-OPEN 이 되고 새로운 측정이 시작된다.") {
                runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(1)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.HALF_OPEN)
            },
            dynamicTest("HALF-OPEN 상태에서 요청을 측정한다 1") {
                circuitBreaker.execute(method = { })

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(1)
                assertThat(delegate.metrics.numberOfSuccessfulCalls).isEqualTo(1)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.HALF_OPEN)
            },
            dynamicTest("다시 10번의 측정이 된후 측정값이 실패 측정률보다 낮으면(=안정적이면) 통계는 초기화되고 서킷브레이커는 CLOSE 된다.") {
                repeat(8) { circuitBreaker.execute(method = { }) }

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(0)
                assertThat(delegate.metrics.numberOfSuccessfulCalls).isEqualTo(0)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.CLOSED)
            }
        )
    }
}
