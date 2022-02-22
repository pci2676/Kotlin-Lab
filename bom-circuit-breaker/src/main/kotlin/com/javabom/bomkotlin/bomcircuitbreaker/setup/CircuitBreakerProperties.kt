package com.javabom.bomkotlin.bomcircuitbreaker.setup

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Validated
@ConstructorBinding
data class CircuitBreakerProperties(
    @field:NotBlank
    var name: String? = null,

    @field:NotNull
    var failureRate: Int? = null, //failureRateThreshold

    var slowCallRate: Int = CircuitBreakerConfig.DEFAULT_SLOW_CALL_RATE_THRESHOLD,//slowCallRateThreshold

    var slowCallDuration: Int = CircuitBreakerConfig.DEFAULT_SLOW_CALL_DURATION_THRESHOLD, //sec. 이 값보다 늦은 시간으로 호출이 이루어지면 늦은 호출로 간주한다.

    @field:NotNull
    var slidingWindowType: String? = null, //TIME_BASED, COUNT_BASED

    @field:NotNull
    var slidingWindowSize: Int? = null, // TIME -> 초 , COUNT -> 개수

    @field:NotNull
    var minimumNumberOfCalls: Int? = null, // 실패비율, 느린호출 비율이 유효해 지는 최소 호출 수

    @field:NotNull
    var waitDurationInOpenState: Int? = null, // open -> halfopen 으로 변하기까지 대기시간 mills

    @field:NotNull
    var permittedNumberOfCallsInHalfOpenState: Int? = null, // half-open 은 무조건 COUNT_BASE 타입으로 생성되기때문에 단위는 호출 수 이다.

    var automaticTransition: Boolean = true,
) {
    fun failureRate(): Float {
        return failureRate!!.toFloat()
    }

    fun slowCallRate(): Float {
        return slowCallRate.toFloat()
    }

    fun waitDurationInOpenState(): Duration {
        return Duration.ofMillis(waitDurationInOpenState!!.toLong())
    }

    fun slowCallDuration(): Duration {
        return Duration.ofSeconds(slowCallDuration.toLong())
    }

    fun slidingWindowType(): CircuitBreakerConfig.SlidingWindowType {
        return CircuitBreakerConfig.SlidingWindowType.valueOf(slidingWindowType!!)
    }
}
