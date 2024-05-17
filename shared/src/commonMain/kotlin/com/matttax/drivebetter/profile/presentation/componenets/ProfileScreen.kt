package com.matttax.drivebetter.profile.presentation.componenets

import androidx.compose.foundation.layout.Box
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import com.matttax.drivebetter.profile.domain.state.AuthState
import com.matttax.drivebetter.profile.domain.state.ProfileEvent
import com.matttax.drivebetter.profile.presentation.ProfileViewModel
import com.matttax.drivebetter.profile.presentation.componenets.screens.AuthErrorScreen
import com.matttax.drivebetter.profile.presentation.componenets.screens.LogInScreen
import com.matttax.drivebetter.profile.presentation.componenets.screens.ProfileDataScreen
import com.matttax.drivebetter.profile.presentation.componenets.screens.RegistrationScreen
import com.matttax.drivebetter.profile.presentation.componenets.screens.UnauthorizedScreen
import com.matttax.drivebetter.ui.common.LoadingScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier,
    snackbarHostState: SnackbarHostState
) {
    val viewState by viewModel.viewState.collectAsState()
    LaunchedEffect(true) {
        viewModel.errorFlow.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }
    Box(modifier) {
        when (val authState = viewState) {
            is AuthState.Loading -> LoadingScreen()
            is AuthState.Error -> AuthErrorScreen(authState.message)
            is AuthState.LoggingIn -> LogInScreen(
                onDone = { email, password ->
                    viewModel.obtainEvent(ProfileEvent.EnterProfile(email, password))
                },
                onBack = {
                    viewModel.obtainEvent(ProfileEvent.AbortAuthorization)
                }
            )

            is AuthState.Registration -> RegistrationScreen(
                onDone = { profile, password ->
                    viewModel.obtainEvent(ProfileEvent.CreateProfile(profile, password))
                },
                onBack = {
                    viewModel.obtainEvent(ProfileEvent.AbortAuthorization)
                }
            )

            is AuthState.Unauthorized -> UnauthorizedScreen(
                onLogIn = { viewModel.obtainEvent(ProfileEvent.LogIn) },
                onSignUp = { viewModel.obtainEvent(ProfileEvent.SignUp) }
            )

            is AuthState.LoggedIn -> ProfileDataScreen(
                profile = authState.profileData,
                onEdit = { viewModel.obtainEvent(ProfileEvent.EditProfile(it)) },
                onChangeAvatar = { viewModel.obtainEvent(ProfileEvent.ChangeAvatar(it)) },
                onLogOut = { viewModel.obtainEvent(ProfileEvent.LogOut) }
            )
        }
    }
}