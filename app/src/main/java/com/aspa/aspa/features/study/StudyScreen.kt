package com.aspa.aspa.features.study

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aspa.aspa.R
import com.aspa.aspa.ui.theme.Blue
import com.aspa.aspa.ui.theme.Gray
import com.aspa.aspa.ui.theme.Gray10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen (){
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("React Hook 완전 정복") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Gray)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Gray10)
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.book),
                            contentDescription = "북 아이콘",
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column(

                        ) {
                            Text("AI 맞춤 학습 콘텐츠",
                                style = MaterialTheme.typography.titleMedium)
                            Text("당신의 로드맵에 최적화 된 학습 자료",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray)
                        }

                    }
                    Row(
                       modifier = Modifier.padding(start = 10.dp,bottom = 30.dp)
                    ) {
                        TimeTag("4-6주")
                        Spacer(Modifier.width(5.dp))
                        StatusTag("진행 예정")
                    }


                }

                Spacer(modifier = Modifier.height(10.dp))
                ContentList(
                    number = 1,
                    title = "기본 개념 이해",
                    description = listOf(
                        "핵심 개념과 용어 정의",
                        "기본 원리와 작동 방식",
                        "실제 사용 사례와 예시"
                    )
                )
                ContentList(
                    number = 2,
                    title = "실습 가이드",
                    description = listOf(
                        "단계별 실습 진행",
                        "코드 예제와 설명",
                        "일반적인 실수와 예외 방법"
                    )
                )

                ContentList(
                    number = 3,
                    title = "심화 학습",
                    description = listOf(
                        "고급 기능과 활용법",
                        "성능 최적화 방법",
                        "베스트 프랙티스"
                    )
                )

                ContentList(
                    number = 4,
                    title = "실전 프로젝트",
                    description = listOf(
                        "실제 프로젝트 구현",
                        "프레임워크 가이드",
                        "프로젝트 작성 팁"
                    )
                )

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(30.dp)
                        .background(Blue, shape = CircleShape),
                        contentAlignment = Alignment.Center
                ){
                    Row(
                        Modifier.padding(horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = "시계 아이콘",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Text("학습 시작하기", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "학습을 완료하면 자동으로 진행률이 업데이트됩니다",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

        }
    )
}