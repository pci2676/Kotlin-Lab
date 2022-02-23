package com.javabom.bomkotlin.entity.letter.model

data class LetterContents(
    val to: String,
    val from: String,
    val title: String,
    val body: String,
    val imageUrl: String? = null,
)
