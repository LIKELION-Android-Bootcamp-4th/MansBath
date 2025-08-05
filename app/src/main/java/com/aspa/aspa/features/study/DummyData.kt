package com.aspa.aspa.features.study

import com.aspa.aspa.model.Study
import com.aspa.aspa.model.StudyDetail


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