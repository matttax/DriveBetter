package com.matttax.drivebetter.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.matttax.drivebetter.ui.common.text.SectionTitle
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun ErrorMessage(
    modifier: Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SectionTitle(message)
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = onRetry,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                        horizontal = 7.dp
                    ),
                text = stringResource(SharedRes.strings.start_ride),
                color = MaterialTheme.colors.surface
            )
        }
    }
}