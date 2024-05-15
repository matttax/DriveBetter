package com.matttax.drivebetter.map.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.matttax.drivebetter.ui.theme.Red
import com.matttax.drivebetter.ui.utils.DateUtils
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun RidingView(
    timeSec: Int,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionTitle(
            text = DateUtils.secondsToTimeString(timeSec),
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = onFinish,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Red
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                        horizontal = 7.dp
                    ),
                text = stringResource(SharedRes.strings.finish_ride),
                color = MaterialTheme.colors.surface
            )
        }
    }
}
