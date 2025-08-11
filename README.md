# ASPA - AI 학습 플랫폼

AI와 함께하는 개인 맞춤 학습 플랫폼입니다.

## 🏗️ 프로젝트 구조

### 📁 주요 디렉토리

```
app/src/main/java/com/aspa/aspa/
├── features/           # 기능별 모듈
│   ├── home/          # 홈 화면
│   ├── login/         # 로그인 화면
│   ├── nickname/      # 닉네임 설정 화면
│   ├── quiz/          # 퀴즈 기능 (예정)
│   ├── roadmap/       # 로드맵 기능 (예정)
│   └── mypage/        # 마이페이지 (예정)
├── navigation/        # 네비게이션 관리
├── ui/               # 공통 UI 컴포넌트
└── MainActivity.kt   # 메인 액티비티 (최소화됨)
```

### 🎯 아키텍처 원칙

1. **모듈화**: 각 기능은 독립적인 모듈로 구성
2. **단일 책임**: 각 파일은 하나의 명확한 책임만 가짐
3. **의존성 분리**: 화면 간 직접적인 의존성 최소화
4. **재사용성**: 공통 컴포넌트는 별도로 분리

## 🚀 개발 가이드

### 새로운 화면 추가하기

1. **Screen 정의 추가**
   ```kotlin
   // navigation/Screen.kt
   object NewScreen : Screen("new_screen")
   ```

2. **화면 컴포넌트 생성**
   ```kotlin
   // features/newscreen/NewScreen.kt
   @Composable
   fun NewScreen(
       onNavigate: () -> Unit = {}
   ) {
       // 화면 구현
   }
   ```

3. **ViewModel 생성** (필요시)
   ```kotlin
   // features/newscreen/NewScreenViewModel.kt
   class NewScreenViewModel : ViewModel() {
       // 비즈니스 로직
   }
   ```

4. **네비게이션에 추가**
   ```kotlin
   // navigation/NavGraph.kt
   composable(Screen.NewScreen.route) {
       NewScreen(
           onNavigate = {
               navController.navigate(Screen.TargetScreen.route)
           }
       )
   }
   ```

### 협업 규칙

#### 📝 코드 스타일
- **Kotlin**: 공식 코딩 컨벤션 준수
- **Compose**: Material Design 3 가이드라인 준수
- **네이밍**: 명확하고 의미있는 이름 사용

#### 🔄 브랜치 전략
- `main`: 프로덕션 코드
- `develop`: 개발 브랜치
- `feature/기능명`: 새로운 기능 개발
- `fix/버그명`: 버그 수정

#### 📋 커밋 메시지
```
feat: 새로운 기능 추가
fix: 버그 수정
refactor: 코드 리팩토링
docs: 문서 수정
style: 코드 스타일 변경
test: 테스트 추가/수정
```

### 🛠️ 기술 스택

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **네비게이션**: Navigation Compose
- **상태 관리**: StateFlow
- **백엔드**: Firebase (Auth, Firestore)
- **의존성 관리**: Gradle Version Catalogs

### 🔧 개발 환경 설정

1. **Java 11 이상 설치**
2. **Android Studio 최신 버전**
3. **Firebase 프로젝트 설정**
   - `google-services.json` 파일을 `app/` 디렉토리에 추가
   - Firebase Console에서 프로젝트 생성

### 📱 현재 구현된 기능

- ✅ Google 로그인
- ✅ 닉네임 설정
- ✅ 홈 화면
- ✅ 네비게이션 시스템

### 🚧 예정된 기능

- 🔄 퀴즈 시스템
- 🔄 학습 로드맵
- 🔄 마이페이지
- 🔄 학습 통계

### 🐛 문제 해결

#### 빌드 오류
1. **Java 환경 확인**: `JAVA_HOME` 설정
2. **의존성 동기화**: `./gradlew clean build`
3. **캐시 클리어**: Android Studio → File → Invalidate Caches

#### Firebase 설정
1. Firebase Console에서 프로젝트 생성
2. Android 앱 등록
3. `google-services.json` 다운로드 및 추가
4. SHA-1 인증서 지문 등록

## 🤝 협업 가이드

### 작업 시작하기
1. `develop` 브랜치에서 `feature/기능명` 브랜치 생성
2. 기능 개발 완료 후 PR 생성
3. 코드 리뷰 후 `develop` 브랜치로 머지

### 코드 리뷰 체크리스트
- [ ] 코드 스타일 준수
- [ ] 기능 정상 동작
- [ ] 에러 처리 포함
- [ ] 테스트 코드 작성 (필요시)
- [ ] 문서 업데이트 (필요시)

### 의사소통
- **이슈 트래킹**: GitHub Issues 사용
- **코드 리뷰**: GitHub Pull Requests
- **문서 공유**: README 및 코드 주석

## 📞 문의

프로젝트 관련 문의사항은 GitHub Issues를 통해 등록해주세요.
