package com.javabom.bomkotlin.racing.ui.request

import java.lang.IllegalArgumentException

class TimeRequest(
    _time: String
) {
    val time: Int = _time.toInt()

    init {
        validateTime()
    }

    private fun validateTime() {
        if (time < 1) {
            throw IllegalArgumentException("경기 시간은 1초 이상이어야 합니다. : 입력받은 시간 ${time}초")
        }
    }
}
