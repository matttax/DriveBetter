package com.matttax.drivebetter.ui.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

object StringUtils {
    object Titles {
        const val PROFILE = "Profile"
        const val CREATE_PROFILE = "Create profile"
        const val LOG_IN = "Log in"
    }
    object Actions {
        const val LOG_OUT = "Log out"
        const val NEXT = "Next"
        const val CHANGE_AVATAR = "Change avatar"
    }
    object ProfileFields {
        const val NAME = "Name"
        const val GENDER = "Gender"
        const val CITY = "City"
        const val AGE = "Age"
        const val DATE_OF_BIRTH = "Date of birth"
        const val LICENSE_NUMBER = "License number"
        const val LICENSE_NUMBER_SHORT = "License"
        const val DATE_OF_ISSUE = "Date of issue"
        const val EXPERIENCE_YEARS = "Years of experience"
        const val RATING = "Rating"
        const val EMAIL = "E-mail"
        const val PASSWORD = "Password"
    }
    object Regex {
        const val NON_BLANK = "(^\$)|(\\s+\$)"
        const val NUMERIC = "[0-9 ]+"
        const val DATE_DD_MM_YYYY = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$"
        const val EMAIL = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    }

    object Months {
        val JANUARY: String
            @Composable
            get() = stringResource(SharedRes.strings.months_january)

        val FEBRUARY: String
            @Composable
            get() = stringResource(SharedRes.strings.months_february)

        val MARCH: String
            @Composable
            get() = stringResource(SharedRes.strings.months_march)

        val APRIL: String
            @Composable
            get() = stringResource(SharedRes.strings.months_april)

        val MAY: String
            @Composable
            get() = stringResource(SharedRes.strings.months_may)

        val JUNE: String
            @Composable
            get() = stringResource(SharedRes.strings.months_june)

        val JULY: String
            @Composable
            get() = stringResource(SharedRes.strings.months_july)

        val AUGUST: String
            @Composable
            get() = stringResource(SharedRes.strings.months_august)

        val SEPTEMBER: String
            @Composable
            get() = stringResource(SharedRes.strings.months_september)

        val OCTOBER: String
            @Composable
            get() = stringResource(SharedRes.strings.months_october)

        val NOVEMBER: String
            @Composable
            get() = stringResource(SharedRes.strings.months_november)

        val DECEMBER: String
            @Composable
            get() = stringResource(SharedRes.strings.months_december)

        fun String.notMonthExceptionMessage() = "$this is not a month"
    }

}
