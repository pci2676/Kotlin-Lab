package com.javabom.bomkotlin.main

import com.javabom.bomkotlin.config.AttributeConverterConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(AttributeConverterConfig::class)
@SpringBootApplication
class AttributeConverterApplicationKt


fun main(vararg args: String) {
    runApplication<AttributeConverterApplicationKt>(*args)
}
