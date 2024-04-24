package com.matttax.drivebetter.history.presentation.componenets.ride

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.matttax.drivebetter.ui.common.text.BodyText
import com.matttax.drivebetter.ui.utils.DateUtils
import com.matttax.drivebetter.ui.utils.DateUtils.timeAsString

@Composable
fun AddressTimeView(
    address: String,
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BodyText(
            text = DateUtils.timestampToLocalDateTime(timestamp).timeAsString()
        )
        BodyText(
            modifier = Modifier.fillMaxWidth(0.7f),
            text = address,
            isImportant = false
        )
    }
}
