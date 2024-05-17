package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.profile.presentation.componenets.RegistrationField
import com.matttax.drivebetter.profile.presentation.constraints.ProfileDataType
import com.matttax.drivebetter.ui.common.text.ButtonText
import com.matttax.drivebetter.ui.common.text.Title
import com.matttax.drivebetter.ui.utils.StringUtils

@Composable
fun LogInScreen(
    onDone: (email: String, password: String) -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = StringUtils.Titles.LOG_IN,
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        RegistrationField(
            title = StringUtils.ProfileFields.EMAIL,
            inputType = ProfileDataType.Email
        ) { p, _ -> email = p }
        RegistrationField(
            title = StringUtils.ProfileFields.PASSWORD,
            inputType = ProfileDataType.Password
        ) { p, _ -> password = p }
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
            Button(
                onClick = {
                    onDone(email, password)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 2.dp,
                            horizontal = 7.dp
                        ),
                    text = StringUtils.Actions.NEXT,
                    color = MaterialTheme.colors.surface
                )
            }
        }
    }
}

