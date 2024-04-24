package com.matttax.drivebetter.ui.utils

object NumericUtils {

    fun Double.toRoundedString(): String {
        return ((this * 100).toInt() / 100.0).toString()
    }

    fun Float.toRoundedString(): String {
        return ((this * 100).toInt() / 100f).toString()
    }

    fun Double.toPercentage(): String {
        return "${toRoundedString()}%"
    }

    fun Float.toPercentage(): String {
        return "${toRoundedString()}%"
    }
}
