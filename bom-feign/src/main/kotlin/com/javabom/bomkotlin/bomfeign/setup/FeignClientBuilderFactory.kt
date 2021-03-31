package com.javabom.bomkotlin.bomfeign.setup

import feign.Client
import feign.Feign
import feign.Logger
import feign.Request
import feign.codec.Decoder
import feign.codec.Encoder
import feign.form.spring.SpringFormEncoder
import feign.optionals.OptionalDecoder
import feign.slf4j.Slf4jLogger
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.logging.LogLevel
import org.springframework.cloud.openfeign.FeignFormatterRegistrar
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class FeignClientBuilderFactory {

    companion object {
        private val conversion = FeignMvcContractFactory.createContract(listOf(defaultTimeFormatter()))
        private val objectMapper = DefaultObjectMapperFactory.getDefaultMapper()
        private val httpMessageConverters = HttpMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
        private val decoder = createDecoder(httpMessageConverters)
        private val encoder = createEncoder(httpMessageConverters)

        fun createBuilder(properties: FeignProperties): Feign.Builder {
            return Feign.builder()
                .client(createClient(properties))
                .logLevel(Logger.Level.FULL)
                .logger(Slf4jLogger())
                .options(createOption(properties))
                .decoder(decoder)
                .encoder(encoder)
                .contract(conversion)
        }

        fun createDecoder(httpMessageConverters: HttpMessageConverters): Decoder {
            return OptionalDecoder(ResponseEntityDecoder(SpringDecoder { httpMessageConverters }))
        }

        fun createEncoder(httpMessageConverters: HttpMessageConverters): Encoder {
            return SpringEncoder(SpringFormEncoder()) { httpMessageConverters }
        }

        /*
        * client의 timeout의 설정과 feign option의 timeout 설정 중 우선순의는 feign option이 먼저 적용된다.
        * option을 설정 하지 않을 시 default option설정이 적용되므로 client timeout 옵션은 feign 내에서 의미가 없다.
        * (client timeout설정과 option의 설정이 다를 경우 매번 client를 재생성하므로 똑같이 설정해준다.)
        * */
        private fun createClient(properties: FeignProperties): Client {
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = properties.maxRequests!!
            dispatcher.maxRequestsPerHost = properties.maxRequestsPerHost!!

            return feign.okhttp.OkHttpClient(
                OkHttpClient.Builder()
                    .connectionPool(
                        ConnectionPool(
                            properties.maxIdleConnections!!,
                            properties.connectionKeepAliveDuration!!,
                            TimeUnit.SECONDS
                        )
                    )
                    .connectTimeout(properties.connectTimeout!!, TimeUnit.SECONDS)
                    .readTimeout(properties.readTimeout!!, TimeUnit.SECONDS)
                    .followRedirects(false)
                    .dispatcher(dispatcher)
                    .build()
            )
        }

        private fun createOption(properties: FeignProperties): Request.Options {
            return Request.Options(
                properties.connectTimeout!!,
                TimeUnit.SECONDS,
                properties.readTimeout!!,
                TimeUnit.SECONDS,
                false
            )
        }

        private fun defaultTimeFormatter(): FeignFormatterRegistrar {

            return FeignFormatterRegistrar { registry: FormatterRegistry ->
                val registrar = DateTimeFormatterRegistrar()
                registrar.setUseIsoFormat(false)
                registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"))
                registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                registrar.registerFormatters(registry)
            }
        }
    }
}
