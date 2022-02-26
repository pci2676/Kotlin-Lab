package com.javabom.effectivekotlin.chap1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DataCopyTest {
    @Test
    fun `data 의 copy 테스트`() {
        //given
        val letter1 = Letter(to = "너에게", from = "내가")
        val letter2 = Letter(to = "나에게", from = "너가")

        val envelop = Envelop(category = "독촉", letters = listOf(letter1, letter2), sticker = Sticker("image"))

        //when
        val copyEnvelop = envelop.copy()

        //then
        assertThat(System.identityHashCode(envelop)).isNotEqualTo(System.identityHashCode(copyEnvelop))
        assertThat(System.identityHashCode(envelop.category)).isEqualTo(System.identityHashCode(copyEnvelop.category))
        assertThat(System.identityHashCode(envelop.letters)).isEqualTo(System.identityHashCode(copyEnvelop.letters))
        assertThat(System.identityHashCode(envelop.letters[0])).isEqualTo(System.identityHashCode(copyEnvelop.letters[0]))
        assertThat(System.identityHashCode(envelop.letters[1])).isEqualTo(System.identityHashCode(copyEnvelop.letters[1]))
        assertThat(System.identityHashCode(envelop.sticker)).isEqualTo(System.identityHashCode(copyEnvelop.sticker))

        //when
        copyEnvelop.category = "선물"
        copyEnvelop.letters[0].body = "선물을"
        copyEnvelop.letters[1].body = "보낸다"
        copyEnvelop.sticker.imageUrl = "선물 이미지"

        //then
        assertThat(envelop.category).isNotEqualTo(copyEnvelop.category)
        assertThat(envelop.category).isEqualTo("독촉")
        assertThat(copyEnvelop.category).isEqualTo("선물")

        assertThat(envelop.letters[0].body).isEqualTo("선물을")
        assertThat(envelop.letters[0].body).isEqualTo(copyEnvelop.letters[0].body)

        assertThat(envelop.letters[1].body).isEqualTo("보낸다")
        assertThat(envelop.letters[1].body).isEqualTo(copyEnvelop.letters[1].body)

        assertThat(envelop.sticker.imageUrl).isEqualTo(copyEnvelop.sticker.imageUrl)
        assertThat(envelop.sticker.imageUrl).isEqualTo("선물 이미지")
    }
}
