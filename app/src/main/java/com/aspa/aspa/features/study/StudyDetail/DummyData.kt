package com.aspa.aspa.features.study.StudyDetail

import com.aspa.aspa.model.StudyDetail

val dummyContentList: List<StudyDetail> = listOf(
    StudyDetail(
        title = "React Hook 기본 개념",
        subtitle = listOf(
            "Hook이란 무엇인가?",
            "기존 클래스형 상태 관리와 비교",
            "useState / useEffect 기초 사용법"
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