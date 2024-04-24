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
fun LargeLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 18.sp,
        fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_text_regular),
        color = MaterialTheme.colors.onSecondary,
        textAlign = TextAlign.Center
    )
}
