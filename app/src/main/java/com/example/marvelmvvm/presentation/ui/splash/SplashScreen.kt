package com.example.marvelmvvm.presentation.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.marvelmvvm.BuildConfig
import com.example.marvelmvvm.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel(),
                 uiState: SplashUiState
) {
    var animationFinished by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (animationFinished) 4.5f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )
    when (uiState) {

        is SplashUiState.Success -> {
            LaunchedEffect(key1 = null) {
                animationFinished = true
                delay(850)
                viewModel.navigateHome() // or directly navigateToRouteNoBack(HomeRoute.route)

            }
        }
        else -> {}
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(if (BuildConfig.FLAVOR != "production") R.drawable.logo_app_dev else R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
            )
        }
    }
}
