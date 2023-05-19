package com.example.marvelmvvm.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.marvelmvvm.nav.NavigationComponent
import com.example.marvelmvvm.presentation.ui.theme.BaseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseTheme {
                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        Scaffold {
            NavigationComponent(navController, it)
        }
    }
}

@Composable
fun mainActivity() = LocalContext.current as MainActivity





