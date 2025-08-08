/**
 * Gemini의 응답 문자열에서 ```json 마크다운 블록을 제거하고
 * 순수 JSON 객체로 파싱하는 유틸 함수.
 *
 * @param {string} raw - Gemini로부터 받은 원본 문자열 응답
 * @return {T} JSON 파싱된 객체
 * @throws {Error} JSON 파싱에 실패할 경우 에러 발생
 */
export function cleanAndParseGeminiResponse<T>(raw: string): T {
  const cleaned = raw
    .replace(/^```json\s*/i, "") // 맨 앞의 ```json 제거
    .replace(/^```/, "") // 맨 앞의 ``` 단독 제거 (혹시 위에서 걸러지지 않은 경우)
    .replace(/```$/, "") // 마지막의 ``` 제거
    .trim(); // 앞뒤 공백 제거

  try {
    return JSON.parse(cleaned) as T;
  } catch (err) {
    console.error("❌ JSON 파싱 실패:", err);
    throw new Error("Gemini 응답에서 유효한 JSON을 파싱할 수 없습니다.");
  }
}
