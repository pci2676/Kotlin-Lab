package com.javabom.bomkotlin.bomcircuitbreaker

import com.javabom.bomkotlin.bomcircuitbreaker.factory.CircuitBreakerBuilderFactory.circuitBreakerBuilder
import com.javabom.bomkotlin.bomcircuitbreaker.setup.BaseCircuitBreaker
import com.javabom.bomkotlin.bomcircuitbreaker.setup.CircuitBreakerProperties
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import java.util.stream.Stream

@Suppress("NonAsciiCharacters")
internal class BaseCircuitBreakerCountBaseTest {
    @Test
    fun `기본적으로 CallNotPermittedException 이 발생하면 fallbackMethod 가 실행된다`() {
        //given
        val circuitBreakerDelegate = CircuitBreaker.ofDefaults("")
        val circuitBreaker = BaseCircuitBreaker(delegate = circuitBreakerDelegate)

        //when
        val fallbackResult = circuitBreaker.execute(
            method = {
                throw CallNotPermittedException.createCallNotPermittedException(circuitBreakerDelegate)
            },
            fallbackMethod = {
                true
            }
        )

        //then
        assertThat(fallbackResult).isTrue
    }

    @Test
    fun `기본적으로 CallNotPermittedException 이 발생하지 않으면 fallbackMethod 가 실행되지 않는다`() {
        //given
        val circuitBreakerDelegate = CircuitBreaker.ofDefaults("")
        val circuitBreaker = BaseCircuitBreaker(delegate = circuitBreakerDelegate)


        //when
        val method = {
            throw IllegalArgumentException()
        }
        val fallbackMethod = { _: Throwable ->
            throw UnsupportedOperationException()
        }

        //then
        assertThrows<IllegalArgumentException> {
            circuitBreaker.execute(
                method = method,
                fallbackMethod = fallbackMethod
            )
        }
    }

    @Test
    fun `exception 없이 실행되는 경우 값을 그대로 반환한다`() {
        //given
        val circuitBreakerDelegate = CircuitBreaker.ofDefaults("")
        val circuitBreaker = BaseCircuitBreaker(delegate = circuitBreakerDelegate)


        //when
        val method = {
            true
        }
        val fallbackMethod = { _: Throwable ->
            throw UnsupportedOperationException()
        }

        //then
        val result = circuitBreaker.execute(
            method = method,
            fallbackMethod = fallbackMethod
        )

        assertThat(result).isTrue
    }

    @Test
    fun `무시할 exception 을 등록하면 실패로 취급하지 않는다`() {
        //given
        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 50, // 50%
            slowCallRate = 10,
            slowCallDuration = 30000,
            slidingWindowType = "COUNT_BASED",
            slidingWindowSize = 10, // 최근 시도 10개만 쌓아둠
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = 2, // 2개씩 실패율 측정
            waitDurationInOpenState = 3,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties, listOf(UnsupportedOperationException::class.java))
        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        //when
        circuitBreaker.execute(method = {
            UnsupportedOperationException()
        })
        circuitBreaker.execute(method = {
            UnsupportedOperationException()
        })

        //then
        assertThat(delegate.metrics.failureRate).isEqualTo(0.0f)
        assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(0)
        assertThat(delegate.state).isEqualTo(CircuitBreaker.State.CLOSED)
    }

    @TestFactory
    fun `서킷브레이커 설정에 따라 OPEN 되는 테스트`(): Stream<DynamicTest> {
        //given
        // 1. 10퍼센트의 실패율이 발생시 OPEN
        // 2. 아래 설정은 10번중 1번 실패시 발생
        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 10, // 10%
            slowCallRate = 10,
            slowCallDuration = 30000,
            slidingWindowType = "COUNT_BASED",
            slidingWindowSize = 10, // 최근 시도 10개만 쌓아둠
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = 10, // 10개씩 실패율 측정
            waitDurationInOpenState = 3,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties)

        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("9번의 실패일 때 열리지 않는다") {
                //given
                val failMethod = { throw IllegalArgumentException() }


                //when
                //9번의 실패
                repeat(9) {
                    runCatching { circuitBreaker.execute(method = failMethod) }
                }

                //then
                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.CLOSED)
            },
            dynamicTest("1번의 성공으로 10번의 조건을 채워 통계 생성된 후 서킷브레이커가 열린다") {
                val successMethod = { true }

                //1번의 성공로 90% 달성
                circuitBreaker.execute(method = successMethod)

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(90.0f)
                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(9)
                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.OPEN)
            }
        )
    }

    @TestFactory
    fun `서킷브레이커의 실패율 계산 최소 단위만큼 실행하지 않으면 실패율 계산을 하지 않는다`(): Stream<DynamicTest> {
        //given
        val failCountPer = 2 // 2개마다 측정
        val windowSize = 3 // 통계는 최대 3개에서만 측정

        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 100, // 100%
            slowCallRate = 10,
            slowCallDuration = 30000,
            slidingWindowType = SlidingWindowType.COUNT_BASED.name,
            slidingWindowSize = windowSize,
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = failCountPer,
            waitDurationInOpenState = 3,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties)

        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("2번의 시도중 첫번째 시도만 하면 실패율 계산은 되어있지 않다.") {
                //when
                runCatching {
                    circuitBreaker.execute(method = {
                        throw UnsupportedOperationException()
                    })
                }

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(-1.0f)
            },
            dynamicTest("2번의 시도중 두번째 시도가 실행되면 계산을 한다.") {
                //when
                circuitBreaker.execute(method = {})

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(50f)
            },
            dynamicTest("최소 측정단위가 넘어간 시점부터 통계를 갱신한다.") {
                //when
                circuitBreaker.execute(method = {})

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(33.333332f)
            },
            dynamicTest("현재 최대 3개까지의 시도에서 통계를 내기때문에 가장 오래된 첫 시도에서 실패한 내역은 통계에서 사라진다") {
                //when
                circuitBreaker.execute(method = {})

                //then
                assertThat(delegate.metrics.failureRate).isEqualTo(0f)
            }
        )
    }

    @TestFactory
    fun `COUNT_BASE 서킷브레이커 OPEN to CLOSE 테스트`(): Stream<DynamicTest> {
        //given
        val openWaitMills = 1

        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 60, // 60%
            slowCallRate = 10,
            slowCallDuration = 30000,
            slidingWindowType = SlidingWindowType.COUNT_BASED.name,
            slidingWindowSize = 10, // 최근 시도 10개만 쌓아둠
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = 10, // 10개씩 실패율 측정
            waitDurationInOpenState = openWaitMills,
            automaticTransition = false
        )

        val delegate = circuitBreakerBuilder(properties)
        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("서킷브레이커 OPEN") {
                repeat(10) {
                    runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }
                }

                assertThat(delegate.metrics.failureRate).isEqualTo(100f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.OPEN)
            },
            dynamicTest("automaticTransition=false 라서 waitDurationInOpenState 시간이 흘러도 요청이 없으면 OPEN 상태이다.") {
                Thread.sleep(openWaitMills.toLong() + 1)

                assertThat(delegate.metrics.failureRate).isEqualTo(100f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.OPEN)
            },
            dynamicTest("waitDurationInOpenState 시간이 흐른뒤 요청이 오면 HALF-OPEN 이 되고 새로운 측정이 시작된다.") {
                runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(1)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.HALF_OPEN)
            },
            dynamicTest("HALF-OPEN 상태에서 요청을 측정한다") {
                circuitBreaker.execute(method = { })

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(1)
                assertThat(delegate.metrics.numberOfSuccessfulCalls).isEqualTo(1)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.HALF_OPEN)
            },
            dynamicTest("다시 10번의 측정이 된후 측정값이 실패 측정률보다 낮으면(=안정적이면) 통계는 초기화되고 서킷브레이커는 CLOSE 된다.") {
                repeat(8) {
                    circuitBreaker.execute(method = { })
                }

                assertThat(delegate.metrics.numberOfFailedCalls).isEqualTo(0)
                assertThat(delegate.metrics.numberOfSuccessfulCalls).isEqualTo(0)
                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.CLOSED)
            }
        )
    }

    @Disabled("Thread.sleep()")
    @TestFactory
    fun `COUNT_BASE automaticTransition=true 테스트`(): Stream<DynamicTest> {
        //given
        val openWaitMills = 100

        val properties = CircuitBreakerProperties(
            name = "testCircuitBreaker",
            failureRate = 60, // 60%
            slowCallRate = 10,
            slowCallDuration = 30000,
            slidingWindowType = SlidingWindowType.COUNT_BASED.name,
            slidingWindowSize = 10, // 최근 시도 10개만 쌓아둠
            permittedNumberOfCallsInHalfOpenState = 10,
            minimumNumberOfCalls = 10, // 10개씩 실패율 측정
            waitDurationInOpenState = openWaitMills,
            automaticTransition = true
        )

        val delegate = circuitBreakerBuilder(properties)

        val circuitBreaker = BaseCircuitBreaker(delegate = delegate)

        return Stream.of(
            dynamicTest("서킷브레이커 OPEN") {
                repeat(10) {
                    runCatching { circuitBreaker.execute(method = { throw UnsupportedOperationException() }) }
                }

                assertThat(delegate.metrics.failureRate).isEqualTo(100f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.OPEN)
            },
            dynamicTest("waitDurationInOpenState=true 지정된 시간이 흐르면 요청이 없어도 HALF_OPEN 상태이다.") {
                Thread.sleep(openWaitMills.toLong() + 1)

                assertThat(delegate.metrics.failureRate).isEqualTo(-1f)

                assertThat(delegate.state).isEqualTo(CircuitBreaker.State.HALF_OPEN)
            },
        )
    }
}
