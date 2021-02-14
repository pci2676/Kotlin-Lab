package com.javabom.bomkotlin.chap4

/**
 * () 로 기본생성자 선언 안해놓으면 알아서 기본생성자 생긴다.
 * 그렇게하면 하위 부 생성자에서도 명시적으로 this() 를 호출하지 않아도 된다.
 */
open class View() {
    var viewName: String? = null
        private set
    var viewOption: String? = null
        private set

    constructor(viewName: String)
            : this() {
        this.viewName = viewName
    }

    constructor(viewName: String, viewOption: String)
            : this() {
        this.viewName = viewName
        this.viewOption = viewOption
    }
}
