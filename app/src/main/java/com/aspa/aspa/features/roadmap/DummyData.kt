package com.aspa.aspa.features.roadmap

import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.model.Section

val sampleRoadmap1 = Roadmap(
    title = "React 완전 정복",
    description = "React의 기초부터 고급 개념까지 체계적으로 학습하는 로드맵입니다.",
    completedSection = 2,
    allSection = 6,
    sections = listOf(
        Section(
            title = "1. JSX 이해하기",
            description = "JSX 문법과 사용법을 익힙니다.",
            concept = "JSX, Babel",
            duration = "1~2일",
            status = true,
        ),
        Section(
            title = "2. 컴포넌트 만들기",
            description = "함수형 컴포넌트와 props의 개념을 학습합니다.",
            concept = "Function Component, Props",
            duration = "2~3일",
            status = true,
        ),
        Section(
            title = "3. 상태와 이벤트",
            description = "useState와 이벤트 핸들링을 다룹니다.",
            concept = "useState, onClick",
            duration = "2~3일",
            status = false,
        ),
        Section(
            title = "4. useEffect 이해",
            description = "사이드 이펙트를 처리하는 방법을 익힙니다.",
            concept = "useEffect",
            duration = "2~3일",
            status = false,
        ),
        Section(
            title = "5. Context API",
            description = "컴포넌트 간 전역 상태를 관리합니다.",
            concept = "Context, Provider",
            duration = "3~4일",
            status = false,
        ),
        Section(
            title = "6. 커스텀 훅 만들기",
            description = "반복되는 로직을 추상화해 재사용 가능한 훅을 만듭니다.",
            concept = "Custom Hook",
            duration = "2~3일",
            status = false,
        )
    )
)

val sampleRoadmap2 = Roadmap(
    title = "Vue 마스터하기",
    description = "Vue 3와 Composition API를 중심으로 Vue를 체계적으로 학습합니다.",
    completedSection = 6,
    allSection = 6,
    sections = listOf(
        Section(
            title = "1. 템플릿 문법",
            description = "Vue 템플릿 문법의 기초를 학습합니다.",
            concept = "v-bind, v-for",
            duration = "1~2일",
            status = true,
        ),
        Section(
            title = "2. 컴포넌트 시스템",
            description = "Vue의 컴포넌트 기반 구조를 이해합니다.",
            concept = "Component, Props",
            duration = "2~3일",
            status = true,
        ),
        Section(
            title = "3. 반응형 시스템",
            description = "reactive, ref 등의 반응형 상태를 배웁니다.",
            concept = "reactive, ref",
            duration = "2~3일",
            status = true,
        ),
        Section(
            title = "4. 라이프사이클 훅",
            description = "Vue 컴포넌트 생명주기를 이해합니다.",
            concept = "onMounted, onUpdated",
            duration = "2~3일",
            status = true,
        ),
        Section(
            title = "5. Provide/Inject",
            description = "컴포넌트 간 데이터 전달 방법을 배웁니다.",
            concept = "provide, inject",
            duration = "2일",
            status = false,
        ),
        Section(
            title = "6. 컴포지션 API",
            description = "setup, watch, computed 등 Composition API를 익힙니다.",
            concept = "setup, watch, computed",
            duration = "3일",
            status = false,
        )
    )
)

val sampleRoadmap3 = Roadmap(
    title = "Kotlin 기초부터 실무까지",
    description = "Kotlin 언어의 문법부터 안드로이드 개발까지 배워보는 실전 로드맵입니다.",
    completedSection = 1,
    allSection = 6,
    sections = listOf(
        Section(
            title = "1. 변수와 타입",
            description = "val/var, 기본 타입에 대해 학습합니다.",
            concept = "val, var, Int, String",
            duration = "1일",
            status = true,
        ),
        Section(
            title = "2. 제어문",
            description = "조건문, 반복문 등 흐름 제어를 익힙니다.",
            concept = "if, when, for, while",
            duration = "1~2일",
            status = false,
        ),
        Section(
            title = "3. 함수와 람다",
            description = "함수 선언과 람다식에 대해 학습합니다.",
            concept = "fun, lambda",
            duration = "1~2일",
            status = false,
        ),
        Section(
            title = "4. 클래스와 객체",
            description = "클래스, 객체지향 프로그래밍의 기초를 배웁니다.",
            concept = "class, object, inheritance",
            duration = "2일",
            status = false,
        ),
        Section(
            title = "5. 컬렉션",
            description = "List, Map 등 Kotlin의 컬렉션을 학습합니다.",
            concept = "List, Map, Set",
            duration = "2일",
            status = false,
        ),
        Section(
            title = "6. 안드로이드 Compose",
            description = "Jetpack Compose를 사용한 UI 작성 기초를 익힙니다.",
            concept = "Composable, Modifier",
            duration = "3일",
            status = false,
        )
    )
)

val sampleRoadmaps = listOf(sampleRoadmap1, sampleRoadmap2, sampleRoadmap3)