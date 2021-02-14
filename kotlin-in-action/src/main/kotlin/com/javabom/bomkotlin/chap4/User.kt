package com.javabom.bomkotlin.chap4

interface User {
    // 추상 프로퍼티 -> 구현체가 해당 프로퍼티를 얻을 방법을 제공해야한다.
    val name: String
}

class PrivateUser(override val name: String) : User {
}

class SubscribingUser(val email: String) : User {
    override val name: String
        get() = email.substringBefore("@")
}

class FacebookUser(val accountId: Int) : User {
    override val name = getFacebookName()

    private fun getFacebookName(): String {
        return accountId.toString()
    }
}

class JavaBomUser(val name: String) {
    var address: String = "unspecified"
        set(value) {
            println(
                """
                Address was changed for $name: "$field" -> "$value" 
                """.trimIndent()
            )
            field = value
        }
}
