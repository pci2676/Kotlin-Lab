# Resilience4j의 Circuit Breaker

## 들어가며

`Resilience4j`는 넷플릭스의 히스트릭스에 영감을 받아 개발된 경량화 Fault Tolerance 라이브러리이다.  
그 중 Circuit Breaker(이하 서킷브레이커) 에 대해 중점적으로 다루어보았다.

## 서킷브레이커 기본 개념

### 서킷브레이커의 상태

서킷 브레이커는 유한한 개수의 상태를 가질 수 있는 장치인 FSM(finite state machine)으로 세가지 일반적인 상태와 세가지 특수 상태로 나뉜다.

일반적인 상태는 다음과 같다.

- CLOSED : 서킷브레이커가 닫혀 있는 상태로 서킷브레이커가 감싼 내부의 프로세스로 요청을 보내고 응답을 받을 수 있다.
- OPEN : 서킷브레이커가 열려 있는 상태로 서킷브레이커는 내부의 프로세스로 요청을 보내지 않는다.
- HALF_OPEN : 서킷브레이커가 열려 있는 상태지만 내부의 프로세스로 요청을 보내고 실패율을 측정해 상태를 CLOSED 혹은 OPEN 상태로 변경한다.

일반적인 상태는 요청의 성공 실패 `metric`을 수집하고 그에 따라 상태가 변한다.(상태변이, state transit)  
`CLOSED`는 `OPEN`으로, `OPEN`은 `HALF_OPEN`으로, `HALF_OPEN`은 `metric`의 **실패율**에 따라
`CLOSED`, `OPEN` 두 상태로 선택하여 상태변이를 한다.

이를 도식화하면 다음과 같다.
![image](https://user-images.githubusercontent.com/13347548/134390294-e894e31a-f4bf-458d-aba2-6b2938b62794.png)

특수 상태는 강제로 상태변이를 하지 않으면 될 수 없다.

- DISABLED : 서킷브레이커를 강제로 `CLOSED`한 상태이다. 하지만 실패율에 따른 상태변화도 없고 후술할 이벤트 발행도 발생하지 않는다.
- FORCED_OPEN : 서킷브레이커를 강제로 `OPEN`한 상태이다. `DISABLED`와 동일하게 상태변화도 없고 이벤트 발행도 하지 않는다.
- METRICS_ONLY : 서킷브레이커를 강제로 `CLOSED`한 상태이다. `DISABLED`과 동일하게 상태변화는 없지만 이벤트 발행을 하고 내부 프로세스의 metric 수집한다.

### 서킷브레이커의 타입

앞서 서킷브레이커가 `metric`을 수집한다고 했다. 수집한 결과는 `Sliding Window`로 원형배열에 수집하고 방식은 두가지로 나뉘게 된다.

#### Count-based sliding window (카운트 기반)

카운트 기반은 n개의 원형배열로 구현된다. 원형 배열의 크기를 10으로 하면 10개의 측정 값을 유지하고 새로운 측정 값이 들어올때마다 가장 오래된 측정 값을 제거한 뒤 총 집계를 갱신한다.

#### Time-based sliding window (시간 기반)

시간 기반도 동일하게 n개의 원형배열로 구현된다. n은 시간(초, epoch second)단위로 10으로 설정하면 1초씩 10개의 부분 집계 버킷가 생긴다. 동일하게 시간이 흐르면 가장 오래된 부분 집계 버킷이
제거되고 총 집계가 갱신된다.

#### 서킷브레이커의 OPEN 상태전이

앞에서 설명한 두 가지 기반 모두 요청이 실패했다고 취급하는 방식은 같다. 취급이라고 한 이유는 요청이 성공한 경우에도 실패로 측정하는 경우가 있기 때문이다. 먼저 일반적으로 `exception`이 발생하면 실패라고
측정한다. 그리고 응답이 성공적이었음에도 `slow call`(느린호출)이라고 인식하면 실패로 측정한다.

예를들어 카운트 기반의 서킷브레이커의 현재 상태가 `CLOSED`, 설정한 **실패율, 느린호출 비율이 임계치가 50퍼센트**일때 **총 4개의 요청**을 보낸다고 가정하자.

각 case는 독립적이다.

- case1. 1개의 요청에서 exception 발생 :arrow_right: 상태변이 없음
- case2. 1개의 요청에서 slow call 발생 :arrow_right: 상태변이 없음
- case3. 1개의 요청에서 exception, 1개의 요청에서 slow call 발생 :arrow_right: 상태변이 없음
- case4. 2개의 요청에서 exception 혹은 slow call 발생 :arrow_right: `OPEN`으로 상태변이
- case5. 1개의 요청에서 exception, 1개의 요청에서 slow call exception 발생 :arrow_right: `OPEN`으로 상태변이

즉, 서킷브레이커의 OPEN 상태전이는 임계치로 지정한 실패율(실패+느린호출 실패) 혹은 느린호출 비율 중 하나만 달성해도 발생한다.

## 서킷브레이커 구현

서킷 브레이커는 직접 코드로 구현하여 사용하는 방법과 애너테이션을 이용해 AOP 방식으로 사용하는 방법으로 나뉘는데 **직접 코드로 구현하는 방법을 선택**했다.

코드 구현을 선택한 이유는 다음과 같다.

1. AOP 방식으로 사용하려면 `application.yaml`에 지정한 서킷브레이커를 비롯해 데코레이팅 할 기능의 애너테이션에 모두 동일하게 이름을 적어줘야 하는데 실수할 여지가 있다.
2. 데코레이팅 할 기능의 순서를 지정하기 쉽다.
3. 특정 `exception`을 블랙리스트, 회이트리스로 등록하는 기능을 사용하기 위해 AOP 방식을 사용하면 해당 `exception`의 풀 패키지 경로를 `application.yaml`에 적어야 하는데 코드로
   구현하면 풀 패키지 경로를 적다가 실수 할 일이 없다.
4. 서킷브레이커에 기능에 대한 테스트에 스프링 컨텍스트가 필요없다.

### 필요 의존성

```groovy
api("io.github.resilience4j:resilience4j-all:1.7.0")
```

`circuit-breaker`가 아닌 `all`을 사용하는 이유는 기능이 많은 `Decorator`가 `all`에 있어서 이다.  
`circuit-breaker`에도 데코레이터가 있지만 기능이 제한적이라 `all`의 데코레이터를 사용하는것이 좋다고 판단했다.

### 값 설정

코드에서 지정할 수 있는 값만 다루도록 하겠다. 이외의 값은
[공식문서](https://resilience4j.readme.io/docs/circuitbreaker#create-and-configure-a-circuitbreaker) 에서 확인 할 수 있다.

- name: 서킷브레이커의 이름
- failureRateThreshold: 실패 비율의 임계치
- slowCallRateThreshold: 느린 호출의 임계치
- slowCallDurationThreshold: 느린 호출로 간주할 시간 값
- slidingWindowType: 서킷브레이커의 타입을 지정한다. `TIME_BASED`, `COUNT_BASED` 중 택 1
- slidingWindowSize: 시간은 단위 초, 카운트는 단위 요청 수
- minimumNumberOfCalls: 총 집계가 유효해 지는 최소한의 요청 수. 이 값이 1000이라면 999번 실패해도 서킷브레이커는 상태변이가 일어나지 않는다.
- waitDurationInOpenState: `OPEN`에서 `HALF_OPEN`으로 상태변이가 실행되는 최소 대기 시간
- permittedNumberOfCallsInHalfOpenState: `HALF_OPEN` 상태에서 총 집계가 유효해지는 최소한의 요청 수. **`COUNT_BASED`로 `slidingWindowType`이
  고정되어 있다.**
- automaticTransition: `true`라면 `waitDurationInOpenState`로 지정한 시간이 지났을 때 새로운 요청이 들어오지 않아도 자동으로 `HALF_OPEN`으로 상태변이가 발생한다.
- ignoreExceptions: 해당 값에 기재한 `exception`은 모두 실패로 집계하지 않는다.
- recordExceptions: 해당 값에 기재한 `exception`은 모두 실패로 집계한다.

#### 설정 및 생성 Sample Code

위에서 설명한 값을 `properties`로 정의한다.

```kotlin
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
```

위 `properties`를 `yaml`을 통해 주입받고 아래 `factory`에서 사용하도록 한다.

서킷브레이커는 `CircuitBreakerRegistry`를 이용해서 등록후 사용하는 방법과 `CircuitBreaker`를 이용하여 직접 생성하는 방법이 있다. 여기서는 직접 생성하는 방식을 택하였다.

`recordExceptions`는 기본 설정에 따라 모든 `exception을` 실패로 간주하게 하였고, `ignoreExceptions`는 설정하는 곳에서 지정할 수 있도록 작성하였다.

그리고 상태변이가 발생하면 `onStateTransition`를 이용해서 상태변이 로그를 남기도록 하였다.

```kotlin
object CircuitBreakerBuilderFactory {
    private val log = logger()

    fun circuitBreaker(
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

        val circuitBreaker = CircuitBreaker.of(circuitBreakerProperties.name!!, config)

        circuitBreaker.eventPublisher.onStateTransition { log.error { it.toString() } }

        return circuitBreaker
    }
}
```

`factory`를 통해 만든 서킷브레이커를 다음 코드와 같이 데코레이터를 이용해서 사용하도록 한다.  
기본적으로 서킷브레이커가 `OPEN`일 때 요청이 처리하면 발생하는 `CallNotPermittedException`이 발생하면 `fallbackMethod`을 실행하도록 하였고 사용하는 쪽에서 추가할 수 있도록
작성했다.

```kotlin
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
}
```

### 생성

앞서 말했듯 서킷브레이커는 `CircuitBreakerRegistry`를 이용해서 등록후 사용하는 방법과 `CircuitBreaker`를 이용하여 직접 생성하는 방법이 있다.

`CircuitBreakerRegistry`를 사용하는 방식을 선택하면 구현체로 `InMemoryCircuitBreakerRegistry`를 사용하게 될 것이고 스레드 세이프하게 구현되어 있다.

`CircuitBreakerRegistry`를 이용하면 다음을 할 수 있다.

1. 등록된 서킷브레이커들의 상태, 통계를 모니터링
2. 서킷브레이커의 등록, 수정, 삭제에 대해 이벤트를 발행

`Spring Framework`를 사용한다면 `io.github.resilience4j:resilience4j-spring-boot2:{version}"` 의존성을 사용하여 `auto-configuration`에
의해 `CircuitBreakerRegistry Bean`을 제공하고 `Spring Actuator`를 이용해서 모니터링을 할 수 있다.

`CircuitBreakerRegistry`를 사용하면 `name`으로 일치하는 서킷브레이커를 찾고 없다면 `name`, `configuration`, `tags`를 조합해서 새로운 서킷브레이커를 저장한다.
여기서 `tags`는 `Spring actuator`를 이용하여 모니터링을 하지 않는다면 입력하지 않아도 상관없다.

```kotlin
circuitBreakerRegistry.circuitBreaker(circuitBreakerName, config, io.vavr.collection.HashMap.ofAll(tags))
```

`CircuitBreaker`를 사용하는 방식은 `CircuitBreakerRegistry`와 마찬가지로 `name`, `configuration`, `tags`를 이용해서 생성하면 된다.

```kotlin
CircuitBreaker.of(circuitBreakerProperties.name!!, config)
```

### 이벤트 발행

이벤트 발행은 서킷브레이커의 `eventPublisher`를 통해 등록할 수 있다. 이벤트 등록을 할 때 주의할 점은 `List`에 `add`되는 구조이므로 **동일한 이벤트를 두번 등록하면 두번 이벤트 발행이
이루어진다.** 이벤트 등록을 유니크하게 하도록 주의해야한다.

```kotlin
circuitBreaker.eventPublisher.onStateTransition { } // 상태변이
circuitBreaker.eventPublisher.onCallNotPermitted { } // 서킷브레이커가 OPEN 일때 요청이 반려되는 경우
circuitBreaker.eventPublisher.onError { } // exception 발생시 
circuitBreaker.eventPublisher.onFailureRateExceeded { } // 실패율 임계치 초과시
circuitBreaker.eventPublisher.onIgnoredError { } // white 리스트로 등록한 exception 발생시
circuitBreaker.eventPublisher.onReset { } // reset 메서드를 이용해서 CLOSED 상태변이를 발생시킬 때
circuitBreaker.eventPublisher.onSlowCallRateExceeded { } // 지연시간비율 임계치 초과시
circuitBreaker.eventPublisher.onSuccess { } // 내부 프로세스 성공시
circuitBreaker.eventPublisher.onEvent { } // 모든 이벤트 발생시
```

### 서킷브레이커 Decorate

`Resilience4j`는 서킷브레이커외에 여러가지 기능을 제공한다.

- Bulkhead : 동시 실행 횟수를 제한한다.
    1. 세마포어 활용 방식
    2. 유한(Bounded) 큐와 스레드 풀을 사용하는 방식
- RateLimiter : 일정시간동안 요청수의 최대치를 제한한다.
- Retry : 요청 실패에 따른 재시도를 관리한다.
- TimeLimiter : `Future`와 함께 사용할때 `timeout`을 할 수 있게 해주고 `timeout` 발생시 `cancel`을 할 수 있도록 한다.
- Cache : `Decorator`에 캐시 인스턴스를 제공하여 사용한다.

`Decorator`에서 체이닝한 순서의 반대로 적용되기 때문에 순서에 유의해야한다. 그리고 `CLOSED`, `HALF_OPEN` 상태일때만 실행된다.
