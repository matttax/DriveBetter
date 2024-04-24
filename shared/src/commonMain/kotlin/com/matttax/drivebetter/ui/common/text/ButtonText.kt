package com.matttax.drivebetter.ui.common.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.example.library.SharedRes

@Composable
fun ButtonText(
    text: String,
    outlined: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 16.sp,
        fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_display_bold),
        color = if (outlined) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        textAlign = TextAlign.Center
    )
}
