package com.aspa.aspa.features.study.StudyDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aspa.aspa.model.ContentDetail
import com.aspa.aspa.ui.theme.Blue
import com.aspa.aspa.ui.theme.Gray
import com.aspa.aspa.ui.theme.Gray10
import com.aspa.aspa.ui.theme.Gray20

@Composable
fun ExpandableContentCard(
    index: Int,
    title: String,
    content: ContentDetail,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Gray10),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(vertical = 20.dp)){

            Row(modifier = Modifier.padding(start = 30.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Blue, shape = CircleShape),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = index.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${title}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier =  Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Gray20)
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(alpha = 0.1f),
                    ),

                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){

                if (expanded) {
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Text(
                        text = "학습개요",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                    Text(
                        text = content.overview,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                    if (content.keyPoints.isNotEmpty()) {
                    Text(
                        text = "핵심 포인트",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) { content.keyPoints.forEach{ point ->
                        Row{
                            Text("•  ", style = MaterialTheme.typography.bodySmall, color = Blue)
                            Text(point, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(height = 5.dp))
                    }
                    }
                    }
                    Text(
                        text = "핵심 내용",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                    Text(
                        text = content.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                }

            }

        }
    }
}

