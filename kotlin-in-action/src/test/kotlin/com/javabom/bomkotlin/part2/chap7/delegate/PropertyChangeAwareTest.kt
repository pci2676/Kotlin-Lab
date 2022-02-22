package com.javabom.bomkotlin.part2.chap7.delegate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PropertyChangeAwareTest {
    @Test
    fun `프로퍼티 변경 통지 테스트`() {
        //given
        val person = Person("찬인", 29, 5300)

        var changed = false
        person.addPropertyChangeListener(listener = { event ->
            println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
            changed = true
        })

        //when
        person.salary = 6000

        //then
        assertThat(changed).isTrue
    }
}
