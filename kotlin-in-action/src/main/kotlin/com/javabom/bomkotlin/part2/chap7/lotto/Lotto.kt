package com.javabom.bomkotlin.part2.chap7.lotto

data class LottoNumber(val number: Int)

operator fun MutableCollection<LottoNumber>.plusAssign(lottoNumber: LottoNumber) {
    this.add(lottoNumber)
}

class LottoTicket(val numbers: List<LottoNumber>)
