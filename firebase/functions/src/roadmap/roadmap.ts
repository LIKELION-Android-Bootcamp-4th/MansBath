import {getFirestore} from "firebase-admin/firestore";
import {Request, Response} from "express";
import {model} from "../ai/gen_ai";
import {ROADMAP_SYSTEM_PROMPT} from "../ai/roadmap_prompt";
import {EXTRA_INSTRUCTION} from "../ai/common_prompt";
import {Roadmap} from "../type/roadmap_types";


// Cloud Function 정의
export const generateRoadmapApp = async (req: Request, res: Response) => {
  try {
    const {uid, questionId} = req.body;

    if (!uid || !questionId) {
      res.status(400).send("uid와 questionId는 필수입니다.");
      return;
    }

    const docRef = getFirestore().doc(`users/${uid}/questions/${questionId}`);
    const docSnap = await docRef.get();

    if (!docSnap.exists) {
      res.status(404).send("해당 질문 문서를 찾을 수 없습니다.");
      return;
    }

    const result = docSnap.data()?.result;
    if (!result) {
      res.status(400).send("result 필드가 존재하지 않습니다.");
      return;
    }

    const resultString = formatResult(result);

    const fullPrompt = `
    ${ROADMAP_SYSTEM_PROMPT}
    [ 사용자 질문 분석서 ] 는 아래와 같아.
    ${resultString}
    ${EXTRA_INSTRUCTION}
    `;

    const response = await model.generateContent({
      contents: [{role: "user", parts: [{text: fullPrompt}]}],
    });

    const aiReply = response.response.text();

    // JSON 파싱 시도
    const parsedRoadmap = cleanAndParseGeminiResponse(aiReply);

    // Firestore에 저장
    const roadmapRef = await getFirestore()
      .collection(`users/${uid}/roadmap`)
      .add(parsedRoadmap);

    res.status(200).json({
      message: "로드맵이 저장되었습니다.",
      docId: roadmapRef.id,
      data: parsedRoadmap,
    });
  } catch (err) {
    console.error("오류 발생:", err);
    res.status(500).send("서버 오류가 발생했습니다.");
  }
};

/**
 * Gemini의 응답 문자열에서 ```json 마크다운 블록을 제거하고
 * 순수 JSON 객체로 파싱하는 유틸 함수.
 *
 * @param {string} raw - Gemini로부터 받은 원본 문자열 응답
 * @return {T} JSON 파싱된 객체
 * @throws {Error} JSON 파싱에 실패할 경우 에러 발생
 */
function cleanAndParseGeminiResponse(raw: string): Roadmap {
  const cleaned = raw
    .replace(/^```json\s*/i, "") // 맨 앞의 ```json 제거
    .replace(/^```/, "") // 맨 앞의 ``` 단독 제거 (혹시 위에서 걸러지지 않은 경우)
    .replace(/```$/, "") // 마지막의 ``` 제거
    .trim(); // 앞뒤 공백 제거

  try {
    return JSON.parse(cleaned);
  } catch (err) {
    console.error("❌ JSON 파싱 실패:", err);
    throw new Error("Gemini 응답에서 유효한 JSON을 파싱할 수 없습니다.");
  }
}

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
function formatResult(result: Record<string, string>): string {
  return Object.entries(result)
    .map(([key, value]) => `- ${key}: ${value}`)
    .join("\n");
}
