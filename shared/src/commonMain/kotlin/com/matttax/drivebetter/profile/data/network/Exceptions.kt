package com.matttax.drivebetter.profile.data.network

class LogInException(override val message: String?) : RuntimeException()
open class RegistrationException(override val message: String?) : RuntimeException()
class IncompleteProfileException : RegistrationException(message = "Profile is missing important fields")

class InvalidTokenException() : RuntimeException("Token not found")
class EditProfileException(override val message: String?) : RuntimeException()
