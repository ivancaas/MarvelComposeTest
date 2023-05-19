package com.example.marvelmvvm.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.marvelmvvm.presentation.ui.detail.DetailRoute
import com.example.marvelmvvm.presentation.ui.home.HomeRoute
import com.example.marvelmvvm.presentation.ui.splash.SplashRoute

@ExperimentalComposeUiApi
@Composable
fun NavigationComponent(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = SplashRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        SplashRoute.composable(this, navHostController)
        HomeRoute.composable(this, navHostController)
        DetailRoute.composable(this, navHostController)
    }
}
