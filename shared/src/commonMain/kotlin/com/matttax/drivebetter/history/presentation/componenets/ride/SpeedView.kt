package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.common.text.SectionTitle
import com.matttax.drivebetter.ui.utils.NumericUtils.toRoundedString
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun SpeedView(
    speed: Double,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BodyText(
            text = title,
            isImportant = false
        )
        SectionTitle(
            text = speed.toRoundedString()
        )
        BodyText(
            text = stringResource(SharedRes.strings.speed_kph),
            isImportant = false
        )
    }
}
