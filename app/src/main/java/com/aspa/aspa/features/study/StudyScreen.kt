package com.aspa.aspa.features.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aspa.aspa.R
import com.aspa.aspa.features.state.UiState
import com.aspa.aspa.model.Study
import com.aspa.aspa.features.study.component.ContentCard
import com.aspa.aspa.features.study.component.StatusTag
import com.aspa.aspa.features.study.component.TimeTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    uiState: UiState<Study>,
    onClickItem: () -> Unit,
) {
    val study: Study? = (uiState as? UiState.Success<Study>)?.data
    val contentList = study?.items ?: emptyList()

    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "학습 생성중...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        is UiState.Success -> {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                study?.title ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                    )
                },
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .padding(top = padding.calculateTopPadding())
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.book),
                                    contentDescription = "북 아이콘",
                                    modifier = Modifier.size(35.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "AI 맞춤 학습 콘텐츠",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        "당신의 로드맵에 최적화 된 학습 자료",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.padding(start = 10.dp, bottom = 30.dp)
                            ) {
                                TimeTag(study?.duration ?: "")
                                Spacer(Modifier.width(5.dp))
                                if (study != null) {
                                    StatusTag(if (study.status) "진행 완료" else "진행 예정")
                                }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp),


                        ) {
                            itemsIndexed(contentList) { index, detail ->
                                ContentCard(
                                    number = index + 1,
                                    title = detail.title,
                                    description = detail.subtitle
                                )
                            }
                            item { Spacer(Modifier.height(10.dp)) }

                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.large
                                        )
                                        .clickable { onClickItem() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.PlayArrow,
                                            contentDescription = "시작",
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            "학습 시작하기",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }

                            item { Spacer(Modifier.height(20.dp)) }

                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "학습을 완료하면 자동으로 진행률이 업데이트됩니다",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }

        is UiState.Failure -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "에러발생. 관리자에게 문의하세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        UiState.Idle -> {}
    }
}
