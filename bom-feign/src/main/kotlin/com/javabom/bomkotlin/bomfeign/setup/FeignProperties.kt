package com.javabom.bomkotlin.bomfeign.setup

import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
data class FeignProperties(
    var maxRequests: Int? = null,
    var maxRequestsPerHost: Int? = null,
    var connectTimeout: Long? = null,
    var readTimeout: Long? = null,
    var maxIdleConnections: Int? = null,
    var connectionKeepAliveDuration: Long? = null,
    var rootUri: String? = null,
    var authKey: String? = null,
)
