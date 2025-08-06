import { GoogleGenerativeAI } from "@google/generative-ai";

// Gemini 설정
const GEMINI_API_KEY = "AIzaSyBa6lj4fuJYF0PUg5aHcdrW19dNNcjj7L8";
const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);

// 모델과 프롬프트를 export하여 다른 파일에서 사용
export const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

export const SYSTEM_PROMPT = `
다음 학습 자료를 바탕으로 5개의 객관식 퀴즈 질문을 한국어로 생성해주세요.
각 질문은 4개의 보기(1개는 정답, 3개는 오답), 정답, 그리고 간략한 설명을 포함해야 합니다.
질문, 보기, 정답, 설명은 제공된 텍스트에서 직접 도출되어야 합니다.
퀴즈는 학습 자료의 다양한 핵심 포인트와 세부 내용을 다루어야 합니다.
개념, 도구, UI 요소에 초점을 맞춰주세요.

학습 자료:
  {
    "conceptDetailFile": {
      "step": 3,
      "title": "화면 사이를 자유롭게 이동하기",
      "learningObjective": "새로운 화면(Activity)을 추가하고, 인텐트(Intent)를 사용하여 화면 간에 이동하는 방법을 학습합니다.",
      "mainContent": {
        "keyPoints": [
          "Activity(액티비티)의 개념 이해",
          "새로운 Activity와 레이아웃 파일 생성하기",
          "인텐트(Intent)를 사용하여 다른 Activity 시작하기"
        ],
        "description": "대부분의 앱은 여러 화면으로 구성됩니다. 이 단계에서는 앱에 두 번째 화면을 추가하고, 첫 화면의 버튼을 클릭했을 때 새로 만든 두 번째 화면으로 넘어가는 방법을 배웁니다. 안드로이드 앱의 핵심 구성요소인 액티비티와 화면 전환을 담당하는 인텐트의 기본 개념을 익힙니다.",
        "details": [
          {
            "title": "안드로이드의 화면 단위: 액티비티",
            "items": [
              "액티비티(Activity)가 앱에서 하나의 화면을 담당하는 컴포넌트임을 이해하기",
              "Android Studio에서 'New > Activity' 메뉴를 통해 새로운 액티비티 생성하기",
              "새 액티비티 생성 시 Kotlin 파일(.kt)과 레이아웃 파일(.xml)이 함께 만들어지는 과정 확인하기",
              "AndroidManifest.xml에 새로운 액티비티가 등록되는 방식 이해하기"
            ]
          },
          {
            "title": "화면 전환의 열쇠: 인텐트",
            "items": [
              "인텐트(Intent)가 다른 컴포넌트(예: 액티비티)를 호출하는 데 사용되는 메시징 객체임을 이해하기",
              "현재 액티비티에서 대상 액티비티로 향하는 명시적 인텐트(Explicit Intent) 생성하기",
              "'startActivity()' 함수에 인텐트를 전달하여 새로운 화면 시작하기"
            ]
          },
          {
            "title": "실습: 화면 전환 기능 구현하기",
            "items": [
              "첫 번째 화면의 버튼 클릭 리스너 내부에 인텐트 생성 및 실행 코드 작성하기",
              "두 번째 화면의 레이아웃 XML 파일에 간단한 텍스트를 추가하여 화면이 전환되었음을 확인하기",
              "앱을 실행하여 버튼 클릭 시 화면이 성공적으로 넘어가는지 테스트하기"
            ]
          }
        ]
      }
    }
  }


출력 형식은 'quizTitle' 키와 'questions' 키를 가진 JSON 객체여야 합니다.
'questions'는 각 질문 객체가 'question', 'options'(문자열 배열), 'answer',
'explanation' 키를 가지는 배열입니다.

`;
