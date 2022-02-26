package com.javabom.effectivekotlin.chap1

data class Letter(
    val to: String,
    val from: String,
    var body: String? = null,
)

class Sticker(
    var imageUrl: String,
)

data class Envelop(
    var category: String,
    val letters: List<Letter>,
    val sticker: Sticker,
)
