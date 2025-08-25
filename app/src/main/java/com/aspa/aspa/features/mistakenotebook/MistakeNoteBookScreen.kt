package com.aspa.aspa.features.mistakenotebook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspa.aspa.features.quiz.component.QuestionCard

@Preview(showBackground = true)
@Composable
fun MistakeNoteBookScreen(){

    Column {
        Row {
            Icon(
                imageVector = Icons.Outlined.AssignmentTurnedIn,
                contentDescription = "오답노트 아이콘",
                modifier = Modifier.size(30.dp),
                tint = Color.Black,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text("약점분석", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text("AI 내용입니다.")
        Spacer(modifier = Modifier.height(5.dp))

//        QuestionCard(
//            questions = ,
//            modifier = Modifier.weight(1f)
//        )


    }
}