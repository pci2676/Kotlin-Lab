package com.javabom.bomkotlin.entity.letter.converter

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.javabom.bomkotlin.entity.letter.model.LetterContents
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class LetterContentsConverter : AttributeConverter<LetterContents, String> {
    override fun convertToDatabaseColumn(attribute: LetterContents): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): LetterContents? {
        return dbData?.let { objectMapper.readValue(dbData, LetterContents::class.java) }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper().apply {
//            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}
