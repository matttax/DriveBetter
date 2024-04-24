package com.matttax.drivebetter.ui.utils

import androidx.compose.runtime.Composable
import com.matttax.drivebetter.ui.utils.StringUtils.Months.notMonthExceptionMessage
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {

    @Composable
    fun LocalDateTime.asSingleString(): String {
        return "${timeAsString()} ${month.asString()}, $dayOfMonth"
    }

    @Composable
    fun LocalDateTime.asTwoStrings(): String {
        return "${dayOfMonth.twoDigits()} ${month.asString()} $year\n${timeAsString()}"
    }

    fun LocalDateTime.timeAsString(): String {
        return "${hour.twoDigits()}:${minute.twoDigits()}"
    }

    private fun Int.twoDigits(): String {
        return if (this < 10) "0$this" else this.toString()
    }

    @Composable
    fun Month.asString(): String {
        return when(this) {
            Month.JANUARY -> StringUtils.Months.JANUARY
            Month.FEBRUARY -> StringUtils.Months.FEBRUARY
            Month.MARCH -> StringUtils.Months.MARCH
            Month.APRIL -> StringUtils.Months.APRIL
            Month.MAY -> StringUtils.Months.MAY
            Month.JUNE -> StringUtils.Months.JUNE
            Month.JULY -> StringUtils.Months.JULY
            Month.AUGUST -> StringUtils.Months.AUGUST
            Month.SEPTEMBER -> StringUtils.Months.SEPTEMBER
            Month.OCTOBER -> StringUtils.Months.OCTOBER
            Month.NOVEMBER -> StringUtils.Months.NOVEMBER
            Month.DECEMBER -> StringUtils.Months.DECEMBER
            else -> throw IllegalArgumentException(this.name.notMonthExceptionMessage())
        }
    }

    fun timestampToLocalDateTime(timestamp: Long): LocalDateTime {
        val dateTime = timestamp.toLocalDateTime()
        return if (dateTime.year == 1970) (timestamp * 1000).toLocalDateTime() else dateTime
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }
}
