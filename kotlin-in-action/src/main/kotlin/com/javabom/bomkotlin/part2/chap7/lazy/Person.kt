package com.javabom.bomkotlin.part2.chap7.lazy


class Person1(val name: String) {
    private var _emails: List<String>? = null

    /**
     * 스레드 세이프 하지 못함
     */
    val emails: List<String>
        get() {
            if (_emails == null) {
                _emails = loadEmails()
            }
            return _emails!!
        }

    private fun loadEmails(): List<String> = listOf("pci2676@gmail.com", "cip0508@naver.com")
}

class Person2(val name: String) {
    /**
     * 스레드 세이프 하다
     */
    val emails by lazy { loadEmails() }

    private fun loadEmails(): List<String> = listOf("pci2676@gmail.com", "cip0508@naver.com")
}
