package com.example.marvelmvvm.presentation.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.marvelmvvm.nav.NavRoute

object DetailRoute : NavRoute<DetailViewModel> {

    override val route = "detail/{characterId}"

    override val title = "DETAILS"

    fun getRouteWithCharacterId(characterId: Int) = route.replace("{characterId}",characterId.toString())

    @Composable
    override fun viewModel(): DetailViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DetailViewModel, navHostController: NavHostController) {
        val uiState by viewModel.uiState.collectAsState()
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        LaunchedEffect(key1 = null){
            val characterId = navBackStackEntry?.arguments?.getString("characterId")
            viewModel.characterId.value = characterId ?: ""
            viewModel.getCharacterData()
        }
        DetailScreen(viewModel, uiState)
    }
}
