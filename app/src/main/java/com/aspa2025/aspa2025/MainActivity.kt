package com.aspa2025.aspa2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aspa2025.aspa2025.features.navigation.AppNavigation
import com.aspa2025.aspa2025.ui.theme.AspaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AspaTheme {
                AppNavigation()
            }
        }
    }
}