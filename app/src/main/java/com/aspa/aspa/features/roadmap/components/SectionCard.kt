package com.aspa.aspa.features.roadmap.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.model.Section

@Composable
fun SectionCard(section: Section, onClick: () -> Unit) {
    val backgroundColor =
        if (section.status) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else MaterialTheme.colorScheme.surface
    val border = if (section.status) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
    val icon = if (section.status) Icons.Default.CheckCircleOutline else Icons.Outlined.Circle
    val iconColor =
        if (section.status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    section.title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                section.concept,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (section.status) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = "완료",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "완료",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
