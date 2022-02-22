package com.javabom.bomkotlin.racing.ui.request

import com.javabom.bomkotlin.racing.domain.creator.RacingCarCreator

class RacerRequest(
    val nameLine: String
) {
    fun toRacingCarCreator(): RacingCarCreator {
        val names = this.nameLine.split(",")
            .toList()
        return RacingCarCreator(names)
    }
}
