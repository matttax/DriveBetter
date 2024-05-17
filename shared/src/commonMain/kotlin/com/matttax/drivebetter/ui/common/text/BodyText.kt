package com.matttax.drivebetter.ui.common.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.fontFamilyResource
import org.example.library.SharedRes

@Composable
fun BodyText(
    text: String,
    isImportant: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        fontFamily = fontFamilyResource(SharedRes.fonts.yandex_sans_text_medium),
        color = if (isImportant) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary,
        textAlign = TextAlign.Start,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}
