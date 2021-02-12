package com.javabom.bomkotlin.chap3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SplitTest {

    @DisplayName("코틀린은 스플릿에 넣은 인자는 정규표현식이 아닌 무조건 딜리미터로 작용한다.")
    @Test
    fun splitTest1() {
        val result = "12.345-5-6.A".split(".")

        assertThat(result).contains("12", "345-5-6", "A")
    }

    @DisplayName("toRegex() 를 이용해서 명시적으로 정규표현식으로 만들수 있다.")
    @Test
    fun splitTest2() {
        val result = "1.3.A".split(".".toRegex())

        assertThat(result).contains("", "", "")
    }

    @DisplayName("확장 함수로 파일 패스 파싱하기")
    @Test
    fun splitInfoTest1() {
        val path = "/Users/coldman/kotlin-book/chapter.adoc"

        val info = parsePath(path)

        assertThat(info).isEqualTo(
            SplitInfo(
                directory = "/Users/coldman/kotlin-book",
                fullName = "chapter.adoc",
                fileName = "chapter",
                extension = "adoc"
            )
        )
    }

    @DisplayName(" \"\"\" 을 이용해서 정규식으로 만들어서 파싱하기 ")
    @Test
    fun splitInfoTest2() {
        val path = "/Users/coldman/kotlin-book/chapter.adoc"

        val info = parsePath2(path)

        assertThat(info).isEqualTo(
            SplitInfo(
                directory = "/Users/coldman/kotlin-book",
                fullName = "chapter.adoc",
                fileName = "chapter",
                extension = "adoc"
            )
        )
    }
}
