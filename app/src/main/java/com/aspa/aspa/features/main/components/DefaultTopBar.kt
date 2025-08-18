package com.aspa.aspa.features.main.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Aspa",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    )
}