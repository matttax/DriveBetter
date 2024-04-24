package com.matttax.drivebetter.ui.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColorUtils {

    @Composable
    fun getBottomColor(isSelected: Boolean): Color {
        return if (isSelected)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onSecondary
    }

    @Composable
    fun getInputFieldColor(matches: Boolean): Color {
        return if (matches)
            MaterialTheme.colors.primaryVariant
        else
            MaterialTheme.colors.error
    }
}
