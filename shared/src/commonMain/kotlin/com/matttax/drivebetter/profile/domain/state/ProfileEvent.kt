package com.matttax.drivebetter.profile.domain.state

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.presentation.avatar.SharedImage

sealed interface ProfileEvent {

    data object LogIn : ProfileEvent
    data object FetchState : ProfileEvent
    data object SignUp : ProfileEvent
    data object LogOut : ProfileEvent
    data object AbortAuthorization : ProfileEvent

    data class CreateProfile(
        val profile: ProfileDomainModel,
        val password: String
    ) : ProfileEvent

    data class EnterProfile(
        val email: String,
        val password: String
    ) : ProfileEvent

    data class EditProfile(
        val profile: ProfileDomainModel
    ) : ProfileEvent

    data class ChangeAvatar(
        val sharedImage: SharedImage
    ) : ProfileEvent
}
