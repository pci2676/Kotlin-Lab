package com.javabom.bomkotlin.bomcircuitbreaker.common

import mu.KLogger
import mu.KotlinLogging

inline fun <reified T : Any> T.logger(): KLogger = KotlinLogging.logger(T::class.java.name)
