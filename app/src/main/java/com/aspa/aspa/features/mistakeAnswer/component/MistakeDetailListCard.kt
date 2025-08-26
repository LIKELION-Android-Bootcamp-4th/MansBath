package com.aspa.aspa.features.mistakeAnswerScreen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MistakeDetailListCard(
    question : String,
    chosen : String,
    answer : String,
    description : String,

){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("문제", style = MaterialTheme.typography.bodyMedium)
            Text("$question", style = MaterialTheme.typography.bodySmall)
            HorizontalDivider(modifier = Modifier.height(12.dp))
            Text("내 답안", style = MaterialTheme.typography.bodyMedium, color = Color.Red.copy(alpha = 0.8f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(color = Color.Red.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .border(width = 1.dp, color = Color.Red.copy(alpha = 0.8f),RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.CenterStart
            ){
                Text("$chosen",style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }
            Text("정답", style = MaterialTheme.typography.bodyMedium, color = Color.Green.copy(alpha = 0.8f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(color = Color.Green.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .border(width = 1.dp, color = Color.Green.copy(alpha = 0.8f),RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.CenterStart
            ){
                Text("$answer",style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }
            Text("해설",style = MaterialTheme.typography.bodyLarge)
            Text("$description",style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

    }
}