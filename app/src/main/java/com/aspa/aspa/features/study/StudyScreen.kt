package com.aspa.aspa.features.study

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspa.aspa.R
<<<<<<< Updated upstream
import com.aspa.aspa.model.Study
import com.aspa.aspa.model.StudyDetail
=======
import com.aspa.aspa.features.study.component.ContentCard
import com.aspa.aspa.features.study.component.StatusTag
import com.aspa.aspa.features.study.component.TimeTag
>>>>>>> Stashed changes
import com.aspa.aspa.ui.theme.Blue
import com.aspa.aspa.ui.theme.Gray
import com.aspa.aspa.ui.theme.Gray10

@OptIn(ExperimentalMaterial3Api::class)

@Composable
@Preview(showBackground = true)
fun StudyScreen (){

    val dummyStudyList = listOf(
        Study(
            title = "실습 가이드",
            description = "이 섹션에서는 단계별 실습과 코드 작성 방법을 다룹니다.",
            duration = "45분",
            status = false,
            items = listOf(
                StudyDetail(
                    title = "단계별 실습 진행",
                    subtitle = listOf(
                        "개요: 실습을 통해 내용을 직접 적용해 봅니다.",
                        "포인트: 각 단계별로 코드를 작성해보며 익힙니다.",
                        "상세 내용: 버튼 생성, 상태 변경 등 단계별 설명 포함.",
                        "예제: 간단한 카운터 앱 실습."
                    ),
                    content = ""
                ),
                StudyDetail(
                    title = "코드 예제와 설명",
                    subtitle = listOf(
                        "개요: 핵심 개념과 연결되는 예제를 작성합니다.",
                        "포인트: 개념 ↔ 코드 연결 이해.",
                        "상세 내용: 버튼 클릭 → State 변경 → UI 갱신 흐름 설명.",
                        "예제: State hoisting 예제 코드 포함."
                    ),
                    content = ""
                ),
                StudyDetail(
                    title = "일반적인 실수와 예외 방법",
                    subtitle = listOf(
                        "개요: 초보자들이 자주 겪는 실수 정리.",
                        "포인트: 코드 예외 및 해결 팁 제공.",
                        "상세 내용: 재조합 이슈, remember 누락, 상태 초기화 등",
                        "예제: 잘못된 코드 → 수정 예제 비교"
                    ),
                    content = ""
                )
            )
        ),
    )
    val contentList = dummyStudyList.first().items


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
<<<<<<< Updated upstream
=======
                ContentCard(
                    number = 1,
                    title = "기본 개념 이해",
                    description = listOf(
                        "핵심 개념과 용어 정의",
                        "기본 원리와 작동 방식",
                        "실제 사용 사례와 예시"
                    )
                )
                ContentCard(
                    number = 2,
                    title = "실습 가이드",
                    description = listOf(
                        "단계별 실습 진행",
                        "코드 예제와 설명",
                        "일반적인 실수와 예외 방법"
                    )
                )

                ContentCard(
                    number = 3,
                    title = "심화 학습",
                    description = listOf(
                        "고급 기능과 활용법",
                        "성능 최적화 방법",
                        "베스트 프랙티스"
                    )
                )

                ContentCard(
                    number = 4,
                    title = "실전 프로젝트",
                    description = listOf(
                        "실제 프로젝트 구현",
                        "프레임워크 가이드",
                        "프로젝트 작성 팁"
                    )
                )
>>>>>>> Stashed changes

                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(contentList) { index, detail ->
                        ContentCard(
                            number = index + 1,
                            title = detail.title,
                            description = detail.subtitle
                        )
                    }
                }
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