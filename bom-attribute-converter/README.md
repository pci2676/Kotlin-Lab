# AttributeConverter를 이용하여 비정형 데이터 저장하기

# 들어가며

이번 포스트에서는 Spring Data JPA를 사용하고 관계형 데이터베이스를 사용하는 환경에서 AttributeConverter를 이용하여 비정형 데이터를 저장하는 방법과 주의점에 대해 살펴보고자 한다.

관계형 데이터베이스에서 비정형 데이터를 저장할 일이 있는가? 할 수 있지만 화면에 뿌려줘야하는 view 성격의 데이터 혹은 구조의 변경이 잦은 데이터라면 사용하게되는 경우가 생긴다.

MySQL과 같은 관계형 데이터 베이스는 각각의 column에 개별 데이터를 저장하는 방식으로 사용된다. 그러다 보니 비정형의 데이터를 저장할 때 하나의 column에 VARCHAR 형식으로 하나의 String 처럼
저장하여 사용해야한다.

# 사용법

이 예제에서는 편지를 나타내는 `Letter` 와

```kotlin
@Entity
@Table(name = "letter")
class Letter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Convert(converter = LetterContentsConverter::class)
    val content: LetterContents? = null,
)
```

편지의 내용을 나타내는 `LetterContents` 를 이용하여 다뤄보겠다.

```kotlin
data class LetterContents(
    val to: String,
    val from: String,
    val title: String,
    val body: String,
)
```

객체를 String으로, String을 객체로 변환해줄 AttributeConverter가 필요하다.(꼭 String이 아니어도 된다.)

```kotlin
@Converter
class LetterContentsConverter : AttributeConverter<LetterContents, String> {
    override fun convertToDatabaseColumn(attribute: LetterContents): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): LetterContents? {
        return dbData?.let { objectMapper.readValue(dbData, LetterContents::class.java) }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}
```

위와 같이 설정하면 끝이다.

# 주의점
주의할 점이 몇가지 있다. 

첫째로, 보통 json 형식으로 저장을 하면서 `ObjectMapper` 를 사용할 경우에 해당한다.  
이때 `setSerializationInclusion(JsonInclude.Include.NON_NULL)` 를 꼭 설정해줘야 한다.  

위 직렬화 설정을 해주지 않으면 `ObjectMapper` 가 `null` 인 프로퍼티를 "null" 로 직렬화를 해버린다. 이게 왜 문제가 되는지 싶겠지만 해당 설정을 해주지 않으면 의도치 않은 update 쿼리가 발생할 수 있다.

`AttributeConverter` 를 사용하는 필드는 데이터 구조의 변경이 잦을 수 있기 때문이다. 즉, 이전에 없던 컬럼이 생길 수 있다는 것이다. 

`LetterContents` 에 imageUrl 이라는 필드가 아래와 같이 새롭게 추가된다고 가정하자. 새롭게 추가되는 필드는 기존 entity 에는 `imageUrl`이 없기 때문에 nullable 하다.


```kotlin
data class LetterContents(
    val to: String,
    val from: String,
    val title: String,
    val body: String,
    val imageUrl: String? = null,
)
```

읽기 작업을 수행하는 프로세스 내에서 기존 `imageUrl` 이 추가되기 이전의 엔티티를 불러왔다가 트랜잭션이 끝나는 시점에서 `ObjectMapper` 가 다시 String 으로 직렬화를 시도하게 된다면 `LetterContents` 에 `imageUrl` 을 "null" 로 직렬화를 하게되고
읽기작업에서 발생하지 말아야 하는 update 쿼리가 발생할 수 있다.

따라서 `null` 을 직렬화 하지 않도록 `setSerializationInclusion(JsonInclude.Include.NON_NULL)` 해주는 것이 좋다.

두번째는 JPA 영속성에 관해서이다.

테스트 코드를 작성하고 실행하면 아래와 같이 AttributeConverter 에서 NullPointerException 이 발생하는 경우가 있다.
```kotlin
Caused by: java.lang.NullPointerException: Parameter specified as non-null is null:
```

물론 여러가지 원인이 있을 수 있지만 다음과 같은 경우를 조심하면 좋다.

OneToMany - ManyToOne 관계로 설정한 Entity 에서 ManyToOne Entity 에 AttributeConverter 를 이용하여 직렬화를 하는 필드가 있다고 하자.

이때 테스트 코드에서 이미 OneToMany 와 연결된 ManyToOne Entity 가 저장되어 있고 OneToMany 엔티티를 JpaRepository 를 이용하여 불러오 ManyToOne 관계를 추가한 뒤 save 를 한다면
`NullPointerException` 이 발생한다. 이는 테스트 코드에서는 Transaction 이 유지되지 않아 Entity 의 상태가 detach 되었기 때문이다.

따라서 테스트 코드에서 불가피하게 트랜잭션을 유지하여 set up 정보를 만들고 싶다면 아래와 같이 헬퍼 클래스를 만들어서 사용하면 쉽게 해결할 수 있다.
```kotlin
@TestComponent
@Transactional
class TestSourceTransactionExecutor {
    fun execute(invoker: () -> Unit) {
        invoker.invoke()
    }
}
```

# 맺으며
NoSQL 을 사용해본적이 없어서 위와 같이 json 형태의 데이터를 생성하고 관리해본적이 없었고 사용할 거라고 생각하지도 못했는데 회사에서 마침 사용할 기회가 생겼다.
개발하고 유지보수하며 자꾸 까먹고 실수하는 부분이라 따로 정리를 했다.  
다른 사람은 나처럼 삽질하지 않길 바란다.
