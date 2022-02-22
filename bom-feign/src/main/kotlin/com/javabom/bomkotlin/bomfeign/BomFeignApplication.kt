package com.javabom.bomkotlin.bomfeign

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BomFeignApplication

fun main(vararg args: String) {
    runApplication<BomFeignApplication>(*args)
}
