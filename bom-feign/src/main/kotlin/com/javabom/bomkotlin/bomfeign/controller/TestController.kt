package com.javabom.bomkotlin.bomfeign.controller

import mu.KLogger
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    var log = logger()

    @GetMapping("/delay/{time}")
    fun delay(@PathVariable(value = "time") time: Long) {
        log.info { "딜레이 시작 : $time" }
        Thread.sleep(time)
        log.info { "딜레이 끝 : $time" }
    }

}

inline fun <reified T : Any> T.logger(): KLogger = KotlinLogging.logger(T::class.java.name)
