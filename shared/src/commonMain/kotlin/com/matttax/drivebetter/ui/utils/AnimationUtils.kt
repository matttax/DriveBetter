package com.matttax.drivebetter.ui.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object AnimationUtils {
    private const val DEFAULT_SLIDE_DURATION = 500
    private const val DEFAULT_FADE_DURATION = 350
    private const val DEFAULT_SCALE_DURATION = 400
    private const val DEFAULT_NAVIGATION_DURATION = 700

    val scaleIn = scaleIn(
        animationSpec = tween(DEFAULT_SCALE_DURATION)
    )

    val scaleOut = scaleOut(
        animationSpec = tween(DEFAULT_SCALE_DURATION)
    )

    val navigationSlideInAnimation = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(DEFAULT_NAVIGATION_DURATION)
    )

    val navigationSlideOutAnimation = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(DEFAULT_NAVIGATION_DURATION)
    )

    fun popUpEnter(popUpDirection: PopUpDirection) = slideInVertically (
        initialOffsetY = {
            when(popUpDirection) {
                PopUpDirection.UP -> it
                PopUpDirection.DOWN -> -it
            }
        },
        animationSpec = tween(
            durationMillis = DEFAULT_SLIDE_DURATION,
            easing = LinearOutSlowInEasing
        )
    ) + fadeIn(
        animationSpec = tween(
            durationMillis = DEFAULT_FADE_DURATION,
            easing = LinearOutSlowInEasing
        )
    )

    fun popUpExit(popUpDirection: PopUpDirection) = slideOutVertically (
        targetOffsetY = {
            when(popUpDirection) {
                PopUpDirection.UP -> it
                PopUpDirection.DOWN -> -it
            }
        },
        animationSpec = tween(
            durationMillis = DEFAULT_SLIDE_DURATION,
            easing = FastOutSlowInEasing
        )
    )  + fadeOut(
        animationSpec = tween(
            durationMillis = DEFAULT_FADE_DURATION,
            easing = FastOutSlowInEasing
        )
    ) + shrinkVertically(
        animationSpec = tween(
            delayMillis = DEFAULT_SLIDE_DURATION
        )
    )

    enum class PopUpDirection {
        UP, DOWN
    }
}
