package com.aspa.aspa.features.quiz

import com.aspa.aspa.model.Quiz

class QuizResultDummy {
    companion object {
        val dummyQuizResult1 = listOf(
            Quiz(
                question = "React에서 컴포넌트의 상태를 관리하기 위해 사용하는 Hook은?",
                choices = listOf("useEffect", "useState", "useContext", "useReducer"),
                answer = "useState",
                chosen = "useState",
                description = "useState는 함수형 컴포넌트에서 상태를 관리하기 위한 기본적인 Hook입니다."
            ),
            Quiz(
                question = "JSX에서 JavaScript 표현식을 사용하려면?",
                choices = listOf("{ }", "[ ]", "( )", "< >"),
                answer = "{ }",
                chosen = "{ }",
                description = "JSX에서는 중괄호 { }를 사용하여 JavaScript 표현식을 작성합니다."
            ),
            Quiz(
                question = "JavaScript에서 변수를 선언할 수 없는 키워드는 무엇인가요?",
                choices = listOf("var", "let", "const", "define"),
                answer = "define",
                chosen = "var",
                description = "'define'은 JavaScript에서 변수를 선언하는 키워드가 아닙니다."
            ),
            Quiz(
                question = "다음 중 JavaScript에서 배열을 생성하는 방법으로 올바른 것은?",
                choices = listOf("let arr = []", "let arr = {}", "let arr = ()", "let arr = <>"),
                answer = "let arr = []",
                chosen = "let arr = {}",
                description = "대괄호([])를 사용하여 배열을 생성할 수 있습니다."
            ),


        )
        val dummyQuizResult2 = listOf(
            Quiz(
                question = "JavaScript에서 함수 표현식을 정의할 때 사용하는 키워드는?",
                choices = listOf("function", "def", "lambda", "proc"),
                answer = "function",
                chosen = "function",
                description = "'function' 키워드는 JavaScript에서 함수를 정의할 때 사용됩니다."
            ),
            Quiz(
                question = "JavaScript에서 typeof 연산자의 결과가 아닌 것은?",
                choices = listOf("string", "number", "boolean", "character"),
                answer = "character",
                chosen = "boolean",
                description = "'typeof' 연산자는 'character'를 반환하지 않습니다."
            ),
            Quiz(
                question = "다음 중 JavaScript에서 truthy 값이 아닌 것은?",
                choices = listOf("0", "[]", "{}", "'false'"),
                answer = "0",
                chosen = "0",
                description = "숫자 0은 falsy 값입니다."
            ),
            Quiz(
                question = "JavaScript에서 '===' 연산자는 어떤 연산자인가요?",
                choices = listOf("값과 타입을 모두 비교", "값만 비교", "타입만 비교", "값과 메모리 위치 비교"),
                answer = "값과 타입을 모두 비교",
                chosen = "값만 비교",
                description = "'===' 연산자는 값과 타입을 모두 비교합니다."
            ),
        )

        val dummyQuizResult3 = listOf(
            Quiz(
                question = "JavaScript의 배열에서 마지막 요소를 제거하는 메서드는?",
                choices = listOf("pop()", "shift()", "remove()", "delete()"),
                answer = "pop()",
                chosen = "shift()",
                description = "'pop()' 메서드는 배열의 마지막 요소를 제거합니다."
            ),
            Quiz(
                question = "JavaScript에서 비동기 처리를 위한 객체는?",
                choices = listOf("Promise", "Future", "Task", "Coroutine"),
                answer = "Promise",
                chosen = "Task",
                description = "'Promise'는 비동기 처리를 위해 사용되는 JavaScript 객체입니다."
            ),
            Quiz(
                question = "JavaScript에서 객체의 속성을 순회할 때 사용하는 반복문은?",
                choices = listOf("for...in", "for...of", "forEach", "while"),
                answer = "for...in",
                chosen = "forEach",
                description = "'for...in'은 객체의 속성을 순회할 때 사용됩니다."
            ),
            Quiz(
                question = "JavaScript에서 이벤트 리스너를 추가하는 메서드는?",
                choices = listOf("addEventListener", "attachEvent", "onEvent", "bindEvent"),
                answer = "addEventListener",
                chosen = "onEvent",
                description = "'addEventListener'는 이벤트 리스너를 등록하는 표준 메서드입니다."
            )
        )

    }

}