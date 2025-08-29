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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.model.Roadmap

@Composable
fun RoadmapCard(roadmap: Roadmap, onClick: () -> Unit) {
    val isCompleted = roadmap.completedSection == roadmap.allSection
    val progress = roadmap.completedSection.toFloat() / roadmap.allSection

    val backgroundColor = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
    val border = if (isCompleted) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    val icon = Icons.Default.CheckCircleOutline
    val iconColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCompleted) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        roadmap.title,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        roadmap.title,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface (
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${roadmap.completedSection}/${roadmap.allSection}",
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                roadmap.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = "완료",
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("완료", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("전체 진도", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${progress.times(100).toInt()}%", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
