package com.aspa.aspa.features.mistakeAnswerScreen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.aspa.aspa.ui.theme.Blue
import com.aspa.aspa.ui.theme.Gray
import com.aspa.aspa.ui.theme.Gray10


@Composable
fun MistakeListCard(
    quizTitle : String,
    currentAt : String,
    index : Int,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Gray10),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = quizTitle , style = MaterialTheme.typography.bodyMedium, color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text("보기", style = MaterialTheme.typography.labelSmall, color = Blue)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "날짜:" , style = MaterialTheme.typography.bodyMedium, color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = currentAt , style = MaterialTheme.typography.bodyMedium, color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row {
                Text("틀린문제:", style = MaterialTheme.typography.bodyMedium, color = Color.Black.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "${index}개", style = MaterialTheme.typography.bodyMedium, color = Color.Red, fontWeight = FontWeight.SemiBold)
            }
        }
    }

}