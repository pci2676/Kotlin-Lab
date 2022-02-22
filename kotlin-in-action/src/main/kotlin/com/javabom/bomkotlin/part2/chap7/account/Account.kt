package com.javabom.bomkotlin.part2.chap7.account

class Account {
    var setting: Setting? = null

    operator fun plus(setting: Setting): Account {
        this.setting = setting
        return this
    }


}

class Setting(val enable: Boolean)
