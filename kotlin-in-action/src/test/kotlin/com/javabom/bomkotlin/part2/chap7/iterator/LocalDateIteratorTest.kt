package com.javabom.bomkotlin.part2.chap7.iterator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LocalDateIteratorTest {
    @Test
    fun `날짜 범위에 대한 이터레이터 구현하기`() {
        //given
        val newYear: LocalDate = LocalDate.ofYearDay(2021, 2)
        val daysOff: ClosedRange<LocalDate> = newYear.minusDays(1)..newYear

        operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
            object : Iterator<LocalDate> {
                var current = start

                override fun hasNext() =
                    current <= endInclusive

                override fun next() = current.apply {
                    current = plusDays(1)
                }
            }
        //when
        val localDates = mutableSetOf<LocalDate>()
        for (dayOff in daysOff) {
            localDates.add(dayOff)
        }

        //then
        assertThat(localDates).contains(
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2021, 1, 2)
        )
    }

}
