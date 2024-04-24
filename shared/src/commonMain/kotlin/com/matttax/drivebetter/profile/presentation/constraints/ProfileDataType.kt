package com.matttax.drivebetter.profile.presentation.constraints

import com.matttax.drivebetter.ui.utils.StringUtils

sealed interface ProfileDataType {

    fun matches(input: String): Boolean

    data class Text(
        val regex: String,
        val shouldMatch: Boolean = true
    ) : ProfileDataType {

        private val _regex = Regex(regex)

        override fun matches(input: String): Boolean {
            return if (shouldMatch)
                input.matches(_regex)
            else
                input.matches(_regex).not()
        }
    }

    data class Numeric(val minValue: Int, val maxValue: Int) : ProfileDataType {
        override fun matches(input: String): Boolean {
            return input.toIntOrNull() in minValue..maxValue
        }
    }

    data class Choice(val list: List<String>) : ProfileDataType {
        override fun matches(input: String): Boolean {
            return list.contains(input)
        }
    }

    object Password : ProfileDataType {
        override fun matches(input: String): Boolean {
            return input.length > 8
        }
    }

    object Email : ProfileDataType {

        private val regex = Regex(StringUtils.Regex.EMAIL)

        override fun matches(input: String): Boolean {
            return input.matches(regex)
        }
    }
}
