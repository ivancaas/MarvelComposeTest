package com.example.marvelmvvm.presentation.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.marvelmvvm.nav.NavRoute

object SplashRoute : NavRoute<SplashViewModel> {

    override val route = "splash/"

    override val title = "splash"

    @Composable
    override fun viewModel(): SplashViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: SplashViewModel, navHostController: NavHostController) {
        val uiState by viewModel.uiState.collectAsState()
        SplashScreen(viewModel, uiState)
    }
}
