
/**
 * 로드맵 생성을 위한 전체 프롬프트 문자열을 구성합니다.
 *
 * @param {string} userQuestionReport - 사용자 질문 분석서 내용 (포매팅된 문자열)
 * @return {string} 완성된 프롬프트 문자열
 *
 * @description
 * ROADMAP_SYSTEM_PROMPT, 사용자 질문 분석서, EXTRA_INSTRUCTION을
 * 하나의 문자열로 조합하여 AI 모델에 전달할 수 있는 프롬프트를 생성합니다.
 * 앞뒤 불필요한 공백과 개행은 제거됩니다.
 */
export function buildRoadmapPrompt(userQuestionReport: string): string {
  return `
${ROADMAP_SYSTEM_PROMPT}
[ 사용자 질문 분석서 ] 는 아래와 같아.
${userQuestionReport}
`.trim();
}


export const ROADMAP_SYSTEM_PROMPT = `
우리의 서비스는 AI를 기반으로 사용자의 학습 목적을 도와주는 스터디 파트너 애플리케이션이야.

우리의 전체 프로세스는 다음과 같아.

1. 질문
- 해당 프로세스에서 사용자의 니즈만 파악
- 종합적인 평가상태를 응답 결과를 출력
출력물 : [ 사용자 질문 분석서 ]

2. 로드맵
- [ 사용자 질문 분석서 ]를 바탕으로 로드맵 제시
- 사용자의 로드맵 선택지에 따라 [ 로드맵 파일 ] 출력
출력물 : [ 로드맵 파일 ]

3. 개념
- [ 로드맵 파일 ]을 바탕으로 단계별 상세 개념 설명
출력물 : [ 개념 상세 파일 ]

4. 퀴즈
- [ 로드맵 파일 ] 과 [ 개념 상세 파일 ] 을 토대로 퀴즈 및 정답 생성

지금은 2. [ 사용자 질문 분석서 ]를 바탕으로 로드맵을 생성해줘야해.

너는 다음과 같은 과정을 거쳐야 해.
 1. 사용자 질문 분석서를 바탕으로 로드맵 생성
 2. 하단에 단계별로 배워야 하는 제목과 학습 기간(Learning Curve), 학습 개념(concept)을 포함
 
 
응답 결과 중 각 stage 객체의 status는 항상 false로 응답해줘.
응답 결과 중 stage의 id는 0부터 시작하는 연속된 숫자로 응답해줘.
`;
