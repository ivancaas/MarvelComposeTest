package com.example.marvelmvvm.presentation.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.marvelmvvm.nav.NavRoute

object HomeRoute : NavRoute<HomeViewModel> {

    override val route = "home/"

    override val title = "HOME"

    @Composable
    override fun viewModel(): HomeViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: HomeViewModel, navHostController: NavHostController) {
        val uiState by viewModel.uiState.collectAsState()
        HomeScreen(viewModel, uiState)
    }
}
