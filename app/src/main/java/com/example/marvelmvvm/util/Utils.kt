package com.example.marvelmvvm.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun <T> Boolean.then(param: T): T? = if (this) param else null

@Composable
fun screenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp
@Composable
fun screenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp
