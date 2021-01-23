package com.javabom.bomkotlin.racing.model

class Position(val distance: Int) {

    internal fun forward(): Position {
        return Position(this.distance + MOVE_DISTANCE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (distance != other.distance) return false

        return true
    }

    override fun hashCode(): Int {
        return distance
    }

    companion object {
        private const val MOVE_DISTANCE: Int = 1
        private const val START_DISTANCE: Int = 0

        fun zero(): Position {
            return Position(START_DISTANCE)
        }
    }
}
