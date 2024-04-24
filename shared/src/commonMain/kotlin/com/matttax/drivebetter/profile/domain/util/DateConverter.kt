package com.matttax.drivebetter.profile.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

object DateConverter {

    fun fromString(dd_mm_yyyy: String): LocalDate? {
        val dateArray = when {
            dd_mm_yyyy.contains(DIVIDER_DOT) -> dd_mm_yyyy.split(DIVIDER_DOT)
            dd_mm_yyyy.contains(DIVIDER_SLASH) -> dd_mm_yyyy.split(DIVIDER_SLASH)
            dd_mm_yyyy.contains(DIVIDER_DASH) -> dd_mm_yyyy.split(DIVIDER_DASH)
            else -> null
        }?.mapNotNull {
            it.toIntOrNull()
        }.takeIf { it?.size == 3 && it[1] <= 12 } ?: return null
        return try {
            LocalDate(
                dayOfMonth = dateArray[0],
                month = Month(dateArray[1]),
                year = dateArray[2],
            )
        } catch (illegalEx: IllegalArgumentException) {
            null
        }
    }

    fun LocalDate.asString(): String {
        return "${dayOfMonth}$DIVIDER_DOT${monthNumber}$DIVIDER_DOT${year}"
    }

    private const val DIVIDER_DOT = "."
    private const val DIVIDER_SLASH = "/"
    private const val DIVIDER_DASH = "-"

}
