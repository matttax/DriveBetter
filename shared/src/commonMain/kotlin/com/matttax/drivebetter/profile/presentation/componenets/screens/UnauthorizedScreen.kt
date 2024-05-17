package com.matttax.drivebetter.profile.presentation.componenets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.text.ButtonText
import com.matttax.drivebetter.ui.common.text.Title
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun UnauthorizedScreen(
    onSignUp: () -> Unit,
    onLogIn: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Title(
            text = stringResource(SharedRes.strings.unauthorized_message),
        )
        Spacer(
            modifier = Modifier.size(25.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = onSignUp,
                shape = MaterialTheme.shapes.large
            ) {
                ButtonText(
                    text = stringResource(SharedRes.strings.sign_up),
                    outlined = true
                )
            }
            Spacer(
                modifier = Modifier.size(15.dp)
            )
            OutlinedButton(
                onClick = onLogIn,
                shape = MaterialTheme.shapes.large
            ) {
                ButtonText(
                    text = stringResource(SharedRes.strings.log_in),
                    outlined = true
                )
            }
        }
    }
}


