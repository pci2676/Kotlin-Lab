package com.javabom.bomkotlin.entity.letter

import com.javabom.bomkotlin.entity.letter.converter.LetterContentsConverter
import com.javabom.bomkotlin.entity.letter.model.LetterContents
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "letter")
class Letter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Convert(converter = LetterContentsConverter::class)
    val content: LetterContents,
)
