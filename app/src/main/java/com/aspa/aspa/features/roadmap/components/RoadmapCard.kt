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
fun RoadmapCard(roadmap: Roadmap) {
    val isCompleted = roadmap.completedSection == roadmap.allSection
    val progress = roadmap.completedSection.toFloat() / roadmap.allSection

    val backgroundColor = if (isCompleted) Color(0xFFF0FDF4) else Color.White
    val border = if (isCompleted) BorderStroke(1.dp, Color(0xFFB9F8CF)) else BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f))
    val icon = Icons.Default.CheckCircleOutline
    val iconColor = Color(0xFF00A63E)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isCompleted) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(roadmap.title, fontWeight = FontWeight.Bold)
                } else {
                    Text(roadmap.title, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(color = Color(0xFFECEEF2), shape = RoundedCornerShape(6.75.dp))
                            .padding(horizontal = 8.dp,),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${roadmap.completedSection}/${roadmap.allSection}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                roadmap.description,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isCompleted) {
                Box(
                    modifier = Modifier
                        .background(color = Color(0xFFECEEF2), shape = RoundedCornerShape(6.75.dp))
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
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("완료", fontSize = 13.sp)
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("전체 진도", fontSize = 12.sp, color = Color.Gray)
                    Text("${progress.times(100).toInt()}%", fontSize = 12.sp)
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color.Black,
                    trackColor = Color.LightGray,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
