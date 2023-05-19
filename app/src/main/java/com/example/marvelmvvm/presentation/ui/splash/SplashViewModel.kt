package com.example.marvelmvvm.presentation.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelmvvm.nav.RouteNavigator
import com.example.marvelmvvm.presentation.ui.home.HomeRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val routeNavigator: RouteNavigator,
) : ViewModel(), RouteNavigator by routeNavigator {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _uiState.update {
                SplashUiState.Success("")
            }
        }
    }

    fun navigateHome(){
        navigateToRouteNoBack(HomeRoute.route)
    }

}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object Error : SplashUiState()
    data class Success(val datos: String) : SplashUiState()
}