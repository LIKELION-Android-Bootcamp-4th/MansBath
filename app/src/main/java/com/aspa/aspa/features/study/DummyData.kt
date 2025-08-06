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
                content = listOf(
                    // index 0 - Hook이란 무엇인가?
                    """
        React의 Hook은 함수형 컴포넌트에서 상태 관리와 생명주기 메서드의 기능을 사용할 수 있도록 도와주는 함수입니다. 
        이전에는 상태를 다루거나 컴포넌트가 mount 또는 update될 때 특정 작업을 실행하려면 클래스형 컴포넌트를 써야 했습니다. 
        하지만 Hook의 등장 이후 함수형 컴포넌트만으로도 훨씬 간결하고 선언적인 방식으로 상태와 로직을 구성할 수 있게 되었습니다.
        """.trimIndent(),

                    // index 1 - 클래스형 상태 관리와 비교
                    """
        클래스형 컴포넌트에서는 this.state와 this.setState를 이용해 상태를 관리했습니다. 
        이 방식은 함수나 콜백을 작성할 때 `this` 바인딩 문제가 자주 발생하며, 코드가 길고 복잡해지는 단점이 있습니다.
        Hook을 사용하면 함수형 컴포넌트에서 더 적은 코드로 같은 기능을 수행할 수 있고, 코드 가독성과 유지보수성이 향상됩니다.
        """.trimIndent(),

                    // index 2 - useState / useEffect 기초 사용법
                    """
        useState는 컴포넌트의 상태를 선언하고 관리하는 데 사용됩니다. 예: val (count, setCount) = useState(0)
        useEffect는 컴포넌트의 생명주기를 흉내 내며, mount/updated/unmount 타이밍에 부수효과를 실행할 수 있습니다.
        예를 들어 useEffect(() => { fetchData() }, [])는 컴포넌트가 처음 렌더링될 때만 호출됩니다.
        이 두 Hook은 가장 기초이자 필수적인 기능입니다.
        """.trimIndent()
                )
            ),
            StudyDetail(
                title = "코드 예제와 설명",
                subtitle = listOf(
                    "개요: 핵심 개념과 연결되는 예제를 작성합니다.",
                    "포인트: 개념 ↔ 코드 연결 이해.",
                    "상세 내용: 버튼 클릭 → State 변경 → UI 갱신 흐름 설명.",
                    "예제: State hoisting 예제 코드 포함."
                ),
                content = listOf(
                    // index 0 - Hook이란 무엇인가?
                    """
        React의 Hook은 함수형 컴포넌트에서 상태 관리와 생명주기 메서드의 기능을 사용할 수 있도록 도와주는 함수입니다. 
        이전에는 상태를 다루거나 컴포넌트가 mount 또는 update될 때 특정 작업을 실행하려면 클래스형 컴포넌트를 써야 했습니다. 
        하지만 Hook의 등장 이후 함수형 컴포넌트만으로도 훨씬 간결하고 선언적인 방식으로 상태와 로직을 구성할 수 있게 되었습니다.
        """.trimIndent(),

                    // index 1 - 클래스형 상태 관리와 비교
                    """
        클래스형 컴포넌트에서는 this.state와 this.setState를 이용해 상태를 관리했습니다. 
        이 방식은 함수나 콜백을 작성할 때 `this` 바인딩 문제가 자주 발생하며, 코드가 길고 복잡해지는 단점이 있습니다.
        Hook을 사용하면 함수형 컴포넌트에서 더 적은 코드로 같은 기능을 수행할 수 있고, 코드 가독성과 유지보수성이 향상됩니다.
        """.trimIndent(),

                    // index 2 - useState / useEffect 기초 사용법
                    """
        useState는 컴포넌트의 상태를 선언하고 관리하는 데 사용됩니다. 예: val (count, setCount) = useState(0)
        useEffect는 컴포넌트의 생명주기를 흉내 내며, mount/updated/unmount 타이밍에 부수효과를 실행할 수 있습니다.
        예를 들어 useEffect(() => { fetchData() }, [])는 컴포넌트가 처음 렌더링될 때만 호출됩니다.
        이 두 Hook은 가장 기초이자 필수적인 기능입니다.
        """.trimIndent()
                )
            ),
            StudyDetail(
                title = "일반적인 실수와 예외 방법",
                subtitle = listOf(
                    "개요: 초보자들이 자주 겪는 실수 정리.",
                    "포인트: 코드 예외 및 해결 팁 제공.",
                    "상세 내용: 재조합 이슈, remember 누락, 상태 초기화 등",
                    "예제: 잘못된 코드 → 수정 예제 비교"
                ),
                content = listOf(
                    // index 0 - Hook이란 무엇인가?
                    """
        React의 Hook은 함수형 컴포넌트에서 상태 관리와 생명주기 메서드의 기능을 사용할 수 있도록 도와주는 함수입니다. 
        이전에는 상태를 다루거나 컴포넌트가 mount 또는 update될 때 특정 작업을 실행하려면 클래스형 컴포넌트를 써야 했습니다. 
        하지만 Hook의 등장 이후 함수형 컴포넌트만으로도 훨씬 간결하고 선언적인 방식으로 상태와 로직을 구성할 수 있게 되었습니다.
        """.trimIndent(),

                    // index 1 - 클래스형 상태 관리와 비교
                    """
        클래스형 컴포넌트에서는 this.state와 this.setState를 이용해 상태를 관리했습니다. 
        이 방식은 함수나 콜백을 작성할 때 `this` 바인딩 문제가 자주 발생하며, 코드가 길고 복잡해지는 단점이 있습니다.
        Hook을 사용하면 함수형 컴포넌트에서 더 적은 코드로 같은 기능을 수행할 수 있고, 코드 가독성과 유지보수성이 향상됩니다.
        """.trimIndent(),

                    // index 2 - useState / useEffect 기초 사용법
                    """
        useState는 컴포넌트의 상태를 선언하고 관리하는 데 사용됩니다. 예: val (count, setCount) = useState(0)
        useEffect는 컴포넌트의 생명주기를 흉내 내며, mount/updated/unmount 타이밍에 부수효과를 실행할 수 있습니다.
        예를 들어 useEffect(() => { fetchData() }, [])는 컴포넌트가 처음 렌더링될 때만 호출됩니다.
        이 두 Hook은 가장 기초이자 필수적인 기능입니다.
        """.trimIndent()
                )
            )
        )
    ),
)