package com.matttax.drivebetter.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import com.matttax.drivebetter.ui.shape.StarShape
import com.matttax.drivebetter.ui.theme.Yellow
import com.matttax.drivebetter.ui.theme.YellowDark

@Composable
fun StarRatingBar(
    rating: Float,
    starSize: Dp,
    starCount: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        repeat(starCount) {
            Box(
                modifier = Modifier
                    .size(starSize)
                    .clip(StarShape())
                    .getRatingGradient(rating - it)
            )
        }
    }
}

fun Modifier.getRatingGradient(rating: Float): Modifier {
    return when {
        rating >= 1 -> background(Yellow)
        rating <= 0 -> background(YellowDark)
        else -> background(
            brush = Brush.horizontalGradient(
                colorStops = arrayOf(
                    (rating % 1f) to Yellow,
                    (rating % 1f) to YellowDark
                )
            )
        )
    }
}
