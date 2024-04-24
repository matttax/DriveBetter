package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.profile.domain.model.Gender
import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.presentation.componenets.RegistrationField
import com.matttax.drivebetter.profile.presentation.constraints.ProfileDataType
import com.matttax.drivebetter.ui.common.text.ButtonText
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.AnimationUtils
import com.matttax.drivebetter.ui.utils.StringUtils

@Composable
fun RegistrationScreen(
    onDone: (profile: ProfileDomainModel, password: String) -> Unit,
    onBack: () -> Unit
) {
    var genderVisible by remember { mutableStateOf(false) }
    var dateOfIssueVisible by remember { mutableStateOf(false) }
    var profile by remember { mutableStateOf(ProfileDomainModel()) }
    var password by remember { mutableStateOf<String?>(null) }
    val profileValid by remember {
        derivedStateOf { profile.isValid.also { println(profile) } }
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = StringUtils.Titles.CREATE_PROFILE,
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        RegistrationField(
            title = StringUtils.ProfileFields.NAME,
            inputType = ProfileDataType.Text(
                regex = StringUtils.Regex.NON_BLANK,
                shouldMatch = false
            ),
        ) { value, matches ->
            if (matches) {
                genderVisible = true
            }
            profile = profile.copy(name = if (matches) value else null)
        }
        AnimatedVisibility(
            visible = genderVisible,
            enter = AnimationUtils.popUpEnter(AnimationUtils.PopUpDirection.DOWN),
            exit = AnimationUtils.popUpExit(AnimationUtils.PopUpDirection.DOWN),
        ) {
            RegistrationField(
                title = StringUtils.ProfileFields.GENDER,
                inputType = ProfileDataType.Choice(Gender.listAll()),
            ) { value, matches ->
                profile = profile.copy(gender = if (matches) value.asGender() else null)
            }
        }
        RegistrationField(
            title = StringUtils.ProfileFields.CITY,
            inputType = ProfileDataType.Text(
                regex = StringUtils.Regex.NON_BLANK,
                shouldMatch = false
            )
        ) { value, matches ->
            profile = profile.copy(city = if (matches) value else null)
        }
        RegistrationField(
            title = StringUtils.ProfileFields.DATE_OF_BIRTH,
            inputType = ProfileDataType.Text(StringUtils.Regex.DATE_DD_MM_YYYY),
        ) { value, matches ->
            if (matches) {
                profile = profile.setDateOfBirth(value)
            }
        }
        RegistrationField(
            title = StringUtils.ProfileFields.LICENSE_NUMBER,
            inputType = ProfileDataType.Text(StringUtils.Regex.NUMERIC),
        ) { value, matches ->
            if (matches) {
                dateOfIssueVisible = true
            }
            profile = profile.copy(driversLicenseId = if (matches) value else null)
        }
        AnimatedVisibility(
            visible = dateOfIssueVisible,
            enter = AnimationUtils.popUpEnter(AnimationUtils.PopUpDirection.DOWN),
            exit = AnimationUtils.popUpExit(AnimationUtils.PopUpDirection.DOWN),
        ) {
            RegistrationField(
                title = StringUtils.ProfileFields.DATE_OF_ISSUE,
                inputType = ProfileDataType.Text(StringUtils.Regex.DATE_DD_MM_YYYY)
            ) { value, matches ->
                if (matches) {
                    profile = profile.setDateOfIssue(value)
                }
            }
        }
        RegistrationField(
            title = StringUtils.ProfileFields.EMAIL,
            inputType = ProfileDataType.Email
        ) { value, matches ->
            profile = profile.copy(email = if (matches) value else null)
        }
        AnimatedVisibility(
            visible = profileValid,
            enter = AnimationUtils.popUpEnter(AnimationUtils.PopUpDirection.DOWN),
            exit = AnimationUtils.popUpExit(AnimationUtils.PopUpDirection.DOWN),
        ) {
            RegistrationField(
                title = StringUtils.ProfileFields.PASSWORD,
                inputType = ProfileDataType.Password
            ) { value, matches ->
                password = if (matches) value else null
            }
        }
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                shape = RoundedCornerShape(20.dp)
            ) {
                ButtonText(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 7.dp
                        ),
                    text = "Back"
                )
            }
            AnimatedVisibility(
                visible = profileValid && password.isNullOrEmpty().not(),
                enter = AnimationUtils.scaleIn,
                exit = AnimationUtils.scaleOut
            ) {
                Button(
                    onClick = {
                        password?.let { onDone(profile, it) }
                    },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    ButtonText(
                        modifier = Modifier
                            .padding(
                                vertical = 2.dp,
                                horizontal = 7.dp
                            ),
                        text = StringUtils.Actions.NEXT
                    )
                }
            }
        }
    }
}
