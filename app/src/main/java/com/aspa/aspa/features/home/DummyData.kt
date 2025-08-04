package com.aspa.aspa.features.home.components

object DummyData {

    /**
     * 더미 ZONE
     * 날짜는 피그마에 반영 안해둬서 안찍습니다.
     * 파이어스토어엔 날짜 있슴.
     *
     * 0804-1429
     * 더미 구조좀 바꿈 기록 불러와야되서
     * 1. 새 화면에서 질문 응답 더미 TODO: 파이어스토어 불러오기
     * 2. 기록 더미
     */

    val conversationFlow: List<ModelMessage> = listOf(
        ModelMessage(
            message = "안녕하세요! 무엇을 배우고 싶으신가요? 학습 목표와 현재 상태를 파악하기 위해 몇 가지 질문을 드릴게요.\n\n" +
                    "첫 번째 질문입니다.\n현재 학습 수준은 어느 정도이신가요?",
            choices = listOf("전혀 모름", "기초 수준", "중급 수준", "고급 수준")
        ),
        ModelMessage(
            message = "알겠습니다. 학습 목적은 무엇인가요?",
            choices = listOf("취미", "학업", "여행", "취업")
        ),
        ModelMessage(
            message = "마지막 질문입니다. 선호하는 학습 방식이 있나요?",
            choices = listOf("텍스트 위주", "영상 위주", "실습 위주", "상관 없음")
        ),
        ModelMessage(
            message = "사용자 분석이 완료되었습니다.",
            result = mapOf(
                "현재 수준" to "중급 수준",
                "학습 목적" to "취업",
                "선호 방식" to "실습 위주",
                "추천 전략" to "실무 프로젝트 위주의 심화 학습을 추천합니다."
            )
        )
    )

    // 저장된 대화 기록 (사용자 답변 포함)
    val dummyChatHistories: Map<String, List<HistoryItem>> = mapOf(
        "일본어를 배우고 싶어" to japaneseConversation,
        "물리학을 배우고 싶어" to physicsConversation,
        "심리학을 배우고 싶어" to psychologyConversation
    )

    // 추가 질문에 대한 응답
    val followUpResponses: List<ModelMessage> = listOf(
        ModelMessage(message = "좋은 질문입니다! 해당 내용에 대해서는 로드맵 생성 후 더 자세히 다룰 예정입니다."),
        ModelMessage(message = "알겠습니다. 그 부분에 대한 정보도 로드맵에 반영하도록 노력하겠습니다."),
        ModelMessage(message = "죄송합니다, 지금은 추가 질문에 답변하기 어렵습니다. 먼저 로드맵을 생성해 주세요.")
    )
}

// 각 대화 내용에 UserHistory 추가 (이전과 동일)
private val japaneseConversation: List<HistoryItem> = listOf(
    UserHistory(message = "일본어를 배우고 싶어"),
    ModelHistory(
        ModelMessage(
            message = "안녕하세요! 일본어 학습의 첫걸음을 함께하게 되어 기쁩니다. 😊\n" +
                    "효과적인 학습 계획을 세우기 위해, 먼저 당신의 학습 목표와 현재 상태를 파악하는 것이 중요해요.\n\n" +
                    "첫 번째 질문입니다.\n현재 일본어 실력은 어느 정도이신가요?",
            choices = listOf("...", "...", "...", "어느 정도 학습 경험이 있어요 (예: JLPT N5 수준)")
        )),
    UserHistory(message = "어느 정도 학습 경험이 있어요 (예: JLPT N5 수준)"),
    ModelHistory(
        ModelMessage(
            message = "네, 알겠습니다. 어느 정도 학습 경험이 있으시군요!\n" +
                    "그렇다면 다음 질문에 답변해 주세요.\n\n" +
                    "일본어를 배우려는 주목적은 무엇인가요?",
            choices = listOf("...", "...", "...", "JLPT와 같은 공인 시험에서 좋은 성적을 얻고 싶어요")
        )),
    UserHistory(message = "JLPT와 같은 공인 시험에서 좋은 성적을 얻고 싶어요"),
    ModelHistory(
        ModelMessage(
            message = "목표가 아주 명확하시군요!\n" +
                    "JLPT 시험 준비를 목표로 하시는군요. 좋습니다.\n\n" +
                    "그렇다면 마지막 질문입니다.\n" +
                    "구체적으로 어떤 레벨을 목표로 공부하고 싶으신가요?",
            choices = listOf("...", "JLPT N3: 어느 정도 자신이 있어서 N3를 목표로 하고 싶어요", "...", "...")
        )),
    UserHistory(message = "JLPT N3: 어느 정도 자신이 있어서 N3를 목표로 하고 싶어요"),
    ModelHistory(
        ModelMessage(
            message = "사용자 분석이 완료되었습니다.",
            result = mapOf("사용자 프로필" to "...", "종합 평가" to "...", "학습 방향 제안" to "...", "추천 전략" to "...")
        ))
)

private val physicsConversation: List<HistoryItem> = listOf(
    UserHistory(message = "물리학을 배우고 싶어"),
    ModelHistory(
        ModelMessage(
            message = "물리학 학습에 오신 것을 환영합니다! 어떤 분야에 관심이 있으신가요?",
            choices = listOf("고전 역학", "양자 역학", "전자기학", "상대성 이론")
        )),
    UserHistory(message = "양자 역학"),
    ModelHistory(
        ModelMessage(
            message = "양자 역학은 매우 흥미로운 분야죠. 학습이 완료되면 분석 결과를 알려드릴게요."
        ))
)

private val psychologyConversation: List<HistoryItem> = listOf(
    UserHistory(message = "심리학을 배우고 싶어"),
    ModelHistory(
        ModelMessage(
            message = "심리학 학습을 도와드릴게요. 가장 관심 있는 심리학 분야는 무엇인가요?",
            choices = listOf("인지 심리학", "발달 심리학", "사회 심리학", "임상 심리학")
        )),
    UserHistory(message = "사회 심리학"),
    ModelHistory(
        ModelMessage(
            message = "사회 심리학을 선택하셨군요. 분석 결과를 곧 보내드리겠습니다.",
            result = mapOf(
                "선택 분야" to "사회 심리학",
                "추천 학습" to "동조, 복종, 집단 역학에 대한 기본 이론부터 학습하는 것을 추천합니다."
            )
        ))
)