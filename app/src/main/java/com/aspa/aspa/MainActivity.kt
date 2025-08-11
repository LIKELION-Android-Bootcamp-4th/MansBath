package com.aspa.aspa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.ui.theme.AspaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AspaTheme {
                MainScreen()
            }
        }
    }
}