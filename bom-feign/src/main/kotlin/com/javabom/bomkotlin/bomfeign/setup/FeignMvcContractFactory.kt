package com.javabom.bomkotlin.bomfeign.setup

import feign.Contract
import org.springframework.cloud.openfeign.FeignFormatterRegistrar
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.core.convert.ConversionService
import org.springframework.format.support.DefaultFormattingConversionService

object FeignMvcContractFactory {
    fun createContract(feignFormatterRegistrarList: List<FeignFormatterRegistrar>): Contract {
        return SpringMvcContract(listOf(), defaultConversionService(feignFormatterRegistrarList))
    }

    private fun defaultConversionService(feignFormatterRegistrarList: List<FeignFormatterRegistrar>): ConversionService {
        val formattingConversionService = DefaultFormattingConversionService()
        for (feignFormatterRegistrar in feignFormatterRegistrarList) {
            feignFormatterRegistrar.registerFormatters(formattingConversionService)
        }
        return formattingConversionService
    }
}
