package com.matttax.drivebetter.ui.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun timestampToHourMinutes(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }
    fun timestampToDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH:mm")

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
}
