package com.javabom.effectivekotlin.chap7


fun chargeWithTryCatch() {
    try {
        chargeThrowException()
        //충전 성공을 기록한다.
    } catch (exception: IllegalStateException) {
        //충전 실패를 기록한다.
    }
}

fun chargeThrowException() {
    throw IllegalStateException("충전실패")
}

fun chargeWithOutTryCatch() {
    when (val result = chargeReturnThrow()) {
        is ChargeResult.Failure -> {
            //충전 실패를 기록한다.
        }
        is ChargeResult.Success -> {
            //충전 성공을 기록한다.
        }
    }
}

fun chargeReturnThrow(): ChargeResult {
    return ChargeResult.Failure(IllegalStateException("충전실패"))
}

sealed class ChargeResult {
    class Success() : ChargeResult()
    class Failure(val throwable: Throwable) : ChargeResult()
}
