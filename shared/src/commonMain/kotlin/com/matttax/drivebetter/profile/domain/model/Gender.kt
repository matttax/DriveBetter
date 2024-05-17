package com.matttax.drivebetter.profile.domain.model

enum class Gender(val text: String) {
    MALE("Male"),
    FEMALE("Female");

    companion object {
        fun listAll(): List<String> {
            return entries.map { it.text }
        }

        fun String.asGender(): Gender? {
            return when(trim()) {
                "Male",
                "male" -> MALE
                "Female",
                "female" -> FEMALE
                else -> null
            }
        }
    }
}
