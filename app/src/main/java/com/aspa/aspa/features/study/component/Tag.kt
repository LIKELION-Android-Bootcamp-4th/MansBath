package com.aspa.aspa.features.study.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeTag(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "시계 아이콘",
                modifier = Modifier.size(11.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun StatusTag(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
