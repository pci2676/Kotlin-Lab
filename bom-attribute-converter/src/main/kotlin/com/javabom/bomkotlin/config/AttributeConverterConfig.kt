package com.javabom.bomkotlin.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(value = ["com.javabom.bomkotlin.entity"])
@EntityScan(value = ["com.javabom.bomkotlin.entity"])
@ComponentScan(value = ["com.javabom.bomkotlin.entity"])
class AttributeConverterConfig {
}
