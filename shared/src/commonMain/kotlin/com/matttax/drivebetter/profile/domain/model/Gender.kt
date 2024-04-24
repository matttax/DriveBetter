package com.matttax.drivebetter.profile.domain.model

enum class Gender(val text: String) {
    MALE("Male"),
    FEMALE("Female");

    companion object {
        fun listAll(): List<String> {
            return entries.map { it.text }
        }

        fun String.asGender(): Gender? {
            return when(this) {
                "Male" -> MALE
                "Female" -> FEMALE
                else -> null
            }
        }
    }
}
