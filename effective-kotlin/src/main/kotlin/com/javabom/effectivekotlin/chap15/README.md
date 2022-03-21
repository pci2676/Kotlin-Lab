# 리시버를 명시적으로 참조하라

무언가를 더 자세하게 설명하기 위해서, 명시적으로 긴 코드를 사용할 때가 있듯이 리시버도 명시적으로 적어주는 것이 좋다.

단지, 짧게 적을 수 있다는 이유만으로 리시버를 제거하지 말자. 여러 개의 리시버가 있는 상황에는 명시적으로 적어주는 것이 좋다.

## 여러 개의 리시버

특히 스코프 내부에 둘 이상의 리시버가 있는 경우, 리시버를 명시적으러 적어주도록 하자.  
이를 지켜주면 코드를 오해하고 작성하거나, 의도하지 않은 방향으로 코드가 동작하는걸 미연에 방지할 수 있다.

AS-IS

```kotlin
class Node(val name: String) {
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply {
                print("Create $name")
            }

    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
```

AS-IS는 `apply`의 잘못된 사용 예 이다. `also`를 이용하여 명시적으로 리시버를 지정했다면 문제가 발생하지 않았을 것이다.

TO-BE

```kotlin
class Node(val name: String) {
    fun makeChild(childName: String) =
        create("$name.$childName")
            .also { // it: Node?
                print("Create ${it?.name}")
            }

    fun create(name: String): Node? = Node(name)
}

fun main() {
    val node = Node("parent")
    node.makeChild("child")
}
```

꼭 `apply`를 써야한다면 `this`라는 리시버가 이름이 겹치게 된다. 이러한 경우 어떠한 리시버를 사용할지 명확하지 않게 되는데 레이블을 사용하면 명시적으로 리시버를 지정할 수 있다. 레이블을 사용하지 않는
다면 가장 가까운 리시버를 사용한다.

TO-BE ver.2

```kotlin
class LabelNode(val name: String) { // this@LabelNode
    fun makeChild(childName: String) =
        create("$name.$childName")
            .apply { //this: Node?
                print("Create ${this@LabelNode.name}")
            }

    fun create(name: String): Node? = Node(name)
}
```

## DSL 마커

코틀린 DSL을 사용할 때 여러 리시버를 가진 요소가 중첩되도 리시버를 명시적으로 붙이지 않는다. DSL의 설계 의도이기 때문이다.

그런데 DSL에서 외부의 함수를 사용하는게 위험한 경우가 있다 아래와 같이 DSL을 이용해서 요소를 작성했을 때를 살펴보자.

```kotlin
fun main() {
    table {
        tr {
            td {
                data = "1번"
            }
            td {
                data = "2번"
            }
        }
        tr {
            td {
                data = "3번"
            }
            td {
                data = "4번"
            }
        }
    }
}
```

`tr` 아래에 `tr`을 또 부르는 것은 외부의 함수를 부를 행위이고 이루어 져서는 안되는 행위이다. 하지만 컴파일 에러가 발생하지 않는다.

```kotlin
fun main() {
    table {
        tr {
            td {
                data = "1번"
            }
            td {
                data = "2번"
            }

            tr { // 컴파일에러가 발생하지 않는다.
                td {
                    data = "3번"
                }
                td {
                    data = "4번"
                }
            }
        }
    }
}
```

이때 `kotlin.DslMarker` 메타 어노테이션을 사용하면 컴파일 레벨에서 이를 방지할 수 있다.

```kotlin
@HtmlDsl
class Table {
    private var tableRows: List<TableRow> = listOf()

    fun tr(tableRowInitializer: TableRow.() -> Unit) {
        tableRows = tableRows + TableRow().apply(tableRowInitializer)
    }
}

@HtmlDsl
class TableRow {
    private var tableDatas: List<TableData> = listOf()

    fun td(tableDataInitializer: TableData.() -> Unit) {
        tableDatas + tableDatas + TableData().apply(tableDataInitializer)
    }
}
```
