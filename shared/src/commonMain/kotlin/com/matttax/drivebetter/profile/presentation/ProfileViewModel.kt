package com.matttax.drivebetter.profile.presentation

import com.matttax.drivebetter.coroutines.provideDispatcher
import com.matttax.drivebetter.profile.domain.ProfileRepository
import com.matttax.drivebetter.profile.domain.state.AuthState
import com.matttax.drivebetter.profile.domain.state.ProfileEvent
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.presentation.avatar.SharedImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.lighthousegames.logging.KmLog

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<AuthState>(AuthState.Loading)
    val viewState = _viewState.asStateFlow()

    private val errorChanel = Channel<String>()
    val errorFlow = errorChanel.receiveAsFlow()

    init {
        fetchState()
    }

    fun obtainEvent(viewEvent: ProfileEvent) {
        when(viewEvent) {
            is ProfileEvent.FetchState -> fetchState()
            is ProfileEvent.LogIn -> _viewState.value = AuthState.LoggingIn
            is ProfileEvent.LogOut -> logOut()
            is ProfileEvent.SignUp -> _viewState.value = AuthState.Registration
            is ProfileEvent.AbortAuthorization -> _viewState.value = AuthState.Unauthorized
            is ProfileEvent.CreateProfile -> createProfile(viewEvent)
            is ProfileEvent.EnterProfile -> enterProfile(viewEvent)
            is ProfileEvent.EditProfile -> editProfile(viewEvent.profile)
            is ProfileEvent.ChangeAvatar -> changeAvatar(viewEvent.sharedImage)
        }
    }

    private fun fetchState() {
        _viewState.value = AuthState.Loading
        viewModelScope.launch(provideDispatcher().io) {
            val profile = repository.isLoggedIn()
            _viewState.value = if (profile == null) {
                AuthState.Unauthorized
            } else {
                log.d { "profile found: $profile" }
                AuthState.LoggedIn(profile)
            }
        }
    }

    private fun createProfile(request: ProfileEvent.CreateProfile) {
        _viewState.value = AuthState.Loading
        viewModelScope.launch {
            if (repository.signUp(request)) {
                log.d { "logging in..." }
                _viewState.value = AuthState.LoggedIn(request.profile)
            } else {
                _viewState.value = AuthState.Unauthorized
                errorChanel.send("Cannot enter profile")
                log.e { "log in error" }
            }
        }
    }

    private fun enterProfile(request: ProfileEvent.EnterProfile) {
        _viewState.value = AuthState.Loading
        viewModelScope.launch(provideDispatcher().io) {
            repository.logIn(request)?.let {
                log.d { "logging in..." }
                _viewState.value = AuthState.LoggedIn(it)
            } ?: run {
                log.d { "log in error" }
                _viewState.value = AuthState.Unauthorized
            }
        }
    }

    private fun editProfile(profile: ProfileDomainModel) {
        _viewState.value = AuthState.Loading
        viewModelScope.launch(provideDispatcher().io) {
            val newModel = repository.edit(profile)
            newModel?.let {
                _viewState.value = AuthState.LoggedIn(it)
            } ?: run {
                errorChanel.send("Edit failed")
                _viewState.value = AuthState.LoggedIn(profile)
            }
        }
    }

    private fun changeAvatar(sharedImage: SharedImage) {
        sharedImage.toByteArray()?.let { array ->
            viewModelScope.launch(provideDispatcher().io) {
                log.d { "saving avatar..." }
                errorChanel.send("Avatar is too big")
                repository.changeAvatar(array)
            }
        }
    }

    private fun logOut() {
        repository.logOut()
        _viewState.value = AuthState.Unauthorized
    }

    companion object {
        private val log = KmLog("ProfileViewModel")
    }
}
