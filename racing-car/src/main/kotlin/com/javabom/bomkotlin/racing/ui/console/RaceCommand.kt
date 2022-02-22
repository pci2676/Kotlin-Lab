package com.javabom.bomkotlin.racing.ui.console

import com.javabom.bomkotlin.racing.ui.request.RacerRequest
import com.javabom.bomkotlin.racing.ui.request.TimeRequest

class RaceCommand {
    companion object {
        fun inputRacerNames(): RacerRequest {
            print("경기에 참여할 레이서의 이름을 입력하시오: ")
            val line = readLine() ?: throw IllegalArgumentException("이름을 입력하십시오.")
            return RacerRequest(line)
        }

        fun inputTime(): TimeRequest {
            print("경기를 진행할 시간을 입력하시오: ")
            val time = readLine() ?: throw IllegalArgumentException("경기시간을 입력하십시오.")
            return TimeRequest(time)
        }
    }
}
