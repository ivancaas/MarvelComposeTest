package com.example.marvelmvvm.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.marvelmvvm.R

@Composable
fun LoadingOverlay() {
    Box(modifier= Modifier
        .fillMaxSize()
        .background(color = Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {

        LoadingAnimated()
    }

}

@Composable
fun LoadingAnimated() {
    val composition by rememberLottieComposition (
        LottieCompositionSpec.RawRes(R.raw.ironman_loading)
    )


    LottieAnimation(
        modifier = Modifier.fillMaxWidth(0.3f),
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}