package com.matttax.drivebetter.profile.domain.state

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthorized : AuthState
    data object LoggingIn : AuthState
    data object Registration : AuthState
    data class Error(val message: String) : AuthState
    data class LoggedIn(val profileData: ProfileDomainModel) : AuthState
}
