/**
 * 객체의 key-value 쌍을 포맷팅된 문자열로 변환합니다.
 *
 * 각 항목은 "- key: value" 형식의 줄로 구성되며, 줄바꿈(\n)으로 연결됩니다.
 *
 * @param {Record<string, string>} result - 문자열 key와 value를 가지는 객체
 * @return {string} 포맷팅된 결과 문자열
 *
 * @example
 * formatResult({ name: "Alice", age: "30" });
 * // 반환값:
 * // - name: Alice
 * // - age: 30
 */
export function formatResultFromFirestore(
  result: Record<string, string>
): string {
  return Object.entries(result)
    .map(([key, value]) => `- ${key}: ${value}`)
    .join("\n");
}
