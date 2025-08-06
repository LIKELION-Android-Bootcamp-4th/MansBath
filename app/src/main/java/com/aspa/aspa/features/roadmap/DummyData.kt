package com.aspa.aspa.features.roadmap

import com.aspa.aspa.model.Quiz
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
            quiz = Quiz(
                question = "JSX의 주요 목적은 무엇인가요?",
                choices = listOf("JavaScript를 빠르게 실행하기 위해", "HTML을 데이터로 다루기 위해", "UI를 선언적으로 표현하기 위해", "CSS를 더 쉽게 작성하기 위해"),
                answer = "UI를 선언적으로 표현하기 위해",
                description = "JSX는 JavaScript에 XML 문법을 도입해 UI를 명확하게 표현할 수 있도록 도와줍니다."
            )
        ),
        Section(
            title = "2. 컴포넌트 만들기",
            description = "함수형 컴포넌트와 props의 개념을 학습합니다.",
            concept = "Function Component, Props",
            duration = "2~3일",
            status = true,
            quiz = Quiz(
                question = "함수형 컴포넌트에서 props는 어떤 역할을 하나요?",
                choices = listOf("이벤트 전달", "상태 저장", "부모로부터 받은 데이터", "렌더링 최적화"),
                answer = "부모로부터 받은 데이터",
                description = "props는 부모 컴포넌트에서 자식 컴포넌트로 데이터를 전달할 때 사용됩니다."
            )
        ),
        Section(
            title = "3. 상태와 이벤트",
            description = "useState와 이벤트 핸들링을 다룹니다.",
            concept = "useState, onClick",
            duration = "2~3일",
            status = false,
            quiz = Quiz(
                question = "useState 훅의 목적은 무엇인가요?",
                choices = listOf("전역 상태 관리", "비동기 처리", "로컬 상태 저장", "데이터 바인딩"),
                answer = "로컬 상태 저장",
                description = "useState는 컴포넌트 내부에서 상태를 선언하고 관리할 수 있게 해줍니다."
            )
        ),
        Section(
            title = "4. useEffect 이해",
            description = "사이드 이펙트를 처리하는 방법을 익힙니다.",
            concept = "useEffect",
            duration = "2~3일",
            status = false,
            quiz = Quiz(
                question = "useEffect의 일반적인 사용 목적은?",
                choices = listOf("렌더링 제어", "이벤트 리스너 생성", "사이드 이펙트 처리", "상태 초기화"),
                answer = "사이드 이펙트 처리",
                description = "useEffect는 API 호출, 타이머 등 렌더링 외적인 작업을 처리할 때 사용됩니다."
            )
        ),
        Section(
            title = "5. Context API",
            description = "컴포넌트 간 전역 상태를 관리합니다.",
            concept = "Context, Provider",
            duration = "3~4일",
            status = false,
            quiz = Quiz(
                question = "Context API를 사용하는 이유는?",
                choices = listOf("성능 향상", "데이터 전역 공유", "코드 분리", "컴포넌트 수명 연장"),
                answer = "데이터 전역 공유",
                description = "Context API는 여러 컴포넌트에서 공통적으로 필요한 데이터를 쉽게 공유할 수 있도록 도와줍니다."
            )
        ),
        Section(
            title = "6. 커스텀 훅 만들기",
            description = "반복되는 로직을 추상화해 재사용 가능한 훅을 만듭니다.",
            concept = "Custom Hook",
            duration = "2~3일",
            status = false,
            quiz = Quiz(
                question = "커스텀 훅을 사용하는 주된 이유는?",
                choices = listOf("코드 보안 강화", "코드 재사용성 향상", "렌더링 속도 향상", "JSX 사용 단순화"),
                answer = "코드 재사용성 향상",
                description = "Custom Hook은 공통 로직을 추출하여 여러 컴포넌트에서 재사용 가능하게 만들어줍니다."
            )
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
            quiz = Quiz(
                question = "v-for 디렉티브의 역할은?",
                choices = listOf("조건부 렌더링", "반복 렌더링", "스타일 적용", "이벤트 바인딩"),
                answer = "반복 렌더링",
                description = "v-for는 배열이나 객체를 기반으로 DOM 요소를 반복해서 렌더링할 때 사용됩니다."
            )
        ),
        Section(
            title = "2. 컴포넌트 시스템",
            description = "Vue의 컴포넌트 기반 구조를 이해합니다.",
            concept = "Component, Props",
            duration = "2~3일",
            status = true,
            quiz = Quiz(
                question = "Props의 역할은 무엇인가요?",
                choices = listOf("자식에서 부모로 데이터 전달", "컴포넌트 간 이벤트 공유", "부모에서 자식으로 데이터 전달", "상태 저장"),
                answer = "부모에서 자식으로 데이터 전달",
                description = "Vue의 props는 부모 컴포넌트가 자식 컴포넌트에게 데이터를 전달하는 방법입니다."
            )
        ),
        Section(
            title = "3. 반응형 시스템",
            description = "reactive, ref 등의 반응형 상태를 배웁니다.",
            concept = "reactive, ref",
            duration = "2~3일",
            status = true,
            quiz = Quiz(
                question = "ref와 reactive의 차이는?",
                choices = listOf("ref는 객체 전용", "reactive는 기본형 전용", "ref는 기본형, reactive는 객체", "둘 다 동일"),
                answer = "ref는 기본형, reactive는 객체",
                description = "ref는 기본형 값을 반응형으로 만들고, reactive는 객체 전체를 반응형으로 처리합니다."
            )
        ),
        Section(
            title = "4. 라이프사이클 훅",
            description = "Vue 컴포넌트 생명주기를 이해합니다.",
            concept = "onMounted, onUpdated",
            duration = "2~3일",
            status = true,
            quiz = Quiz(
                question = "onMounted는 언제 호출되나요?",
                choices = listOf("컴포넌트 생성 직후", "DOM 렌더링 완료 후", "컴포넌트 제거 전", "데이터 변경 시"),
                answer = "DOM 렌더링 완료 후",
                description = "onMounted 훅은 DOM이 마운트된 후 실행됩니다."
            )
        ),
        Section(
            title = "5. Provide/Inject",
            description = "컴포넌트 간 데이터 전달 방법을 배웁니다.",
            concept = "provide, inject",
            duration = "2일",
            status = false,
            quiz = Quiz(
                question = "provide/inject의 주요 목적은?",
                choices = listOf("글로벌 상태 공유", "상위-하위 간 데이터 전달", "컴포넌트 재사용", "템플릿 최적화"),
                answer = "상위-하위 간 데이터 전달",
                description = "provide와 inject는 중간 컴포넌트를 거치지 않고도 데이터를 전달할 수 있게 합니다."
            )
        ),
        Section(
            title = "6. 컴포지션 API",
            description = "setup, watch, computed 등 Composition API를 익힙니다.",
            concept = "setup, watch, computed",
            duration = "3일",
            status = false,
            quiz = Quiz(
                question = "setup 함수는 어떤 역할을 하나요?",
                choices = listOf("렌더링 제어", "템플릿 연결", "반응형 로직 정의", "스타일 구성"),
                answer = "반응형 로직 정의",
                description = "setup 함수는 Composition API의 진입점으로 반응형 로직과 훅들을 선언합니다."
            )
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
            quiz = Quiz(
                question = "val과 var의 차이는 무엇인가요?",
                choices = listOf("둘 다 수정 가능", "val은 수정 가능, var은 불가능", "val은 불변, var은 가변", "모두 불변"),
                answer = "val은 불변, var은 가변",
                description = "val은 변경 불가능한 참조를, var은 변경 가능한 참조를 의미합니다."
            )
        ),
        Section(
            title = "2. 제어문",
            description = "조건문, 반복문 등 흐름 제어를 익힙니다.",
            concept = "if, when, for, while",
            duration = "1~2일",
            status = false,
            quiz = Quiz(
                question = "when 표현식은 어떤 역할을 하나요?",
                choices = listOf("예외 처리", "조건 분기", "반복 처리", "클래스 선언"),
                answer = "조건 분기",
                description = "when은 if-else를 대체하는 Kotlin의 조건 분기 표현식입니다."
            )
        ),
        Section(
            title = "3. 함수와 람다",
            description = "함수 선언과 람다식에 대해 학습합니다.",
            concept = "fun, lambda",
            duration = "1~2일",
            status = false,
            quiz = Quiz(
                question = "람다식의 특징은?",
                choices = listOf("이름이 필요함", "여러 줄만 가능", "익명 함수", "클래스에서만 사용 가능"),
                answer = "익명 함수",
                description = "람다는 이름 없이 전달 가능한 함수로써 코드를 간결하게 만듭니다."
            )
        ),
        Section(
            title = "4. 클래스와 객체",
            description = "클래스, 객체지향 프로그래밍의 기초를 배웁니다.",
            concept = "class, object, inheritance",
            duration = "2일",
            status = false,
            quiz = Quiz(
                question = "Kotlin에서 상속을 허용하려면 어떤 키워드를 사용해야 하나요?",
                choices = listOf("inherit", "extends", "open", "super"),
                answer = "open",
                description = "Kotlin의 클래스는 기본적으로 final이며 상속을 허용하려면 open 키워드를 사용해야 합니다."
            )
        ),
        Section(
            title = "5. 컬렉션",
            description = "List, Map 등 Kotlin의 컬렉션을 학습합니다.",
            concept = "List, Map, Set",
            duration = "2일",
            status = false,
            quiz = Quiz(
                question = "mutableListOf는 어떤 컬렉션인가요?",
                choices = listOf("불변 리스트", "가변 리스트", "정렬 리스트", "null 리스트"),
                answer = "가변 리스트",
                description = "mutableListOf는 요소를 추가, 제거할 수 있는 리스트입니다."
            )
        ),
        Section(
            title = "6. 안드로이드 Compose",
            description = "Jetpack Compose를 사용한 UI 작성 기초를 익힙니다.",
            concept = "Composable, Modifier",
            duration = "3일",
            status = false,
            quiz = Quiz(
                question = "Composable 함수는 무엇을 의미하나요?",
                choices = listOf("데이터 저장 함수", "UI를 구성하는 함수", "백엔드 연결 함수", "상태 저장 함수"),
                answer = "UI를 구성하는 함수",
                description = "Composable은 Compose에서 UI 요소를 구성하기 위해 사용하는 함수입니다."
            )
        )
    )
)


val sampleRoadmaps = listOf(sampleRoadmap1, sampleRoadmap2, sampleRoadmap3)