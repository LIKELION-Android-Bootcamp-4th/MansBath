<div align="center">
<img height="500" alt="1" src="https://github.com/user-attachments/assets/e81b307f-e62b-4044-8148-17432389e238" />
<img height="500" alt="2" src="https://github.com/user-attachments/assets/a3be085a-ec11-4696-a81c-7bcb7c235d03" />
</div>

<br>

<div align="center">
<img height="420" alt="3" src="https://github.com/user-attachments/assets/4b34052c-22bb-4ed3-985a-5a3d5f5839cf" />
<img height="420" alt="4" src="https://github.com/user-attachments/assets/f50e9877-af58-4baa-a9f6-4a3ac9cf1771" />
<img height="420" alt="5" src="https://github.com/user-attachments/assets/6349f015-6105-4b56-b465-d486ab14c053" />
<img height="420" alt="6" src="https://github.com/user-attachments/assets/61db446e-ed88-4ea9-89aa-74cae1f75602" />
<img height="420" alt="7" src="https://github.com/user-attachments/assets/4119cef9-9442-4467-8b95-cb13b20a3ebf" />
</div>

# Aspa

AI 기반 개인 맞춤 학습 도우미 Android 애플리케이션입니다. 사용자의 질문과 학습 기록을 바탕으로 AI가 내용을 분석하고, 로드맵·학습 콘텐츠·퀴즈를 자동으로 생성하여 효율적인 자기주도 학습을 돕습니다. Firebase(인증/데이터/함수)와 Google Gemini를 활용해 안정적인 백엔드와 빠른 AI 응답을 제공합니다.

## 주요 기능

- 대화형 학습 챗: 질문을 입력하면 AI가 맥락을 반영해 답변하고 대화 히스토리를 저장합니다.
- 로드맵 생성/관리: 질문 분석 결과로 개인 맞춤 학습 로드맵을 자동 생성하고 상세를 확인/진행합니다.
- 학습 콘텐츠: 로드맵 섹션별 맞춤 학습 카드(제목/설명/소요 시간/진행 상태) 제공 및 학습 시작/진행률 반영.
- 퀴즈 생성: 학습한 개념을 바탕으로 섹션 퀴즈를 자동 생성하고 결과를 저장/조회합니다.
- 실수노트 분석: 오답·실수 기록을 AI로 분석/요약하여 약점을 파악하고 보완 포인트를 제안합니다.
- 홈 위젯: 로드맵/상태 위젯으로 홈 화면에서 진행 상황 확인 및 빠른 이동 지원.
- 소셜 로그인: Google, Kakao, Naver 계정으로 간편 인증을 지원합니다.
- 푸시 알림: 학습 리마인드와 상태 변화 등을 FCM으로 안내합니다.

## 사용 기술

- Android/Kotlin: Jetpack Compose(Material3), Navigation, ViewModel, Coroutines/Flow, DataStore, AppWidget, Splash, 권한 처리 등 최신 안드로이드 컴포넌트 기반.
- 의존성 주입: Hilt(Dagger)로 ViewModel/Repository/RemoteDataSource를 주입.
- Firebase
  - Authentication: Google/Kakao/Naver 소셜 로그인 연동.
  - Firestore: 사용자별 질문/로드맵/학습/퀴즈/실수노트 데이터 저장 및 조회.
  - Cloud Functions(v2, `onCall`): AI 프롬프트 처리, 로드맵/퀴즈/실수노트 생성, 트리거(학습/퀴즈) 및 스케쥴러(알림)처리.
  - Cloud Messaging(FCM): 학습 리마인드/상태 알림 전송.
- AI: Google Gemini(Generative AI) 사용. 프롬프트 빌더/응답 파서/포맷터 구성으로 안정적인 결과 파이프라인 구현.
- 언어/런타임: Android 앱(Kotlin), 함수 서버(TypeScript/Node.js)。

## 아키텍처 개요

- Presentation: Compose UI + ViewModel 상태 기반 화면(`Home`, `Roadmap`, `Study`, `Quiz`, `MistakeAnswer`, `Login` 등).
- Domain/Data: `Repository`가 `RemoteDataSource(Firestore/Functions)`를 래핑하고 `Result`로 예외를 캡슐화.
- Backend(Functions): 질문→리포트→로드맵/학습/퀴즈 생성 흐름을 함수 단위로 분리(`question`, `roadmap`, `quiz`, `mistakenotebook`).
- 상태/영속: Firebase Auth/Firestore + 로컬 DataStore를 조합해 사용자별 진행 상태를 유지.

## 폴더 구조(요약)

```
app/
  src/main/java/com/aspa/aspa/
    features/ ... 화면/네비게이션/위젯
    data/ ... repository/remote/dto
    model/ ... 앱 도메인 모델
    network/ ... Retrofit 프로바이더
    di/ ... Hilt 모듈
firebase/functions/
  src/ai ... 프롬프트/GenAI 모듈
  src/question|roadmap|quiz|mistakenotebook ... 기능별 HTTP(onCall) 함수
  src/Study ... 학습 관련 함수/트리거
```

## 시작하기

1) 사전 준비

- Firebase 콘솔에서 프로젝트 생성 및 Android 앱 등록(`applicationId` 확인).
- `google-services.json`을 `app/`에 배치.
- Firebase Authentication에 Google/Kakao/Naver 제공자 설정.
- Firestore/Functions/FCM 활성화, 보안 규칙/인덱스 적용(`firebase/firestore.rules`, `firebase/firestore.indexes.json`).

2) Functions 환경 변수

- `firebase/functions/.env` 또는 환경 변수에 아래 키를 설정:

```
GEMINI_API_KEY=YOUR_GEMINI_API_KEY
```

3) 빌드/실행

- Android Studio에서 프로젝트 열기 → Gradle 동기화 → 디바이스 선택 → 실행.
- Firebase 에뮬레이터를 활용하는 경우 `firebase emulators:start`로 로컬 테스트 가능(선택).

## 주요 흐름

- 질문 처리: 앱 → Functions `question` → 대화 히스토리/AI 응답 저장 → 앱에 응답 반환.
- 로드맵 생성: 앱 → Functions `generateRoadmap` → 질문 리포트 기반 프롬프트 → 로드맵 저장/ID 반환.
- 학습/퀴즈: 로드맵 섹션별 학습 콘텐츠/퀴즈를 생성·저장하고 존재 여부 체크로 UI 제어.
- 실수노트: 문서 ID 기반으로 AI 분석을 수행하고 요약 결과를 저장/표시.

## 참고

- Gradle: Kotlin DSL 사용(`build.gradle.kts`, `settings.gradle.kts`).
- 권한: Android 13+ 알림 권한 요청 플로우 포함.
- 위젯: 로드맵/상태 위젯으로 빠른 진입 지원.
