import {HttpsError} from "firebase-functions/https";
import {getAiModel} from "../ai/gen_ai";
import {getMistakeAnswerList} from "./firestore_service";
import {SYSTEM_PROMPT} from "./prompt";
import {Mistake} from "../type/mistake";

/**
 * 저장된 데이터를 파싱 한 뒤, 데이터 추출
 * @param {string} uid - 사용자 아이디
 * @param {string} docId - 문서 아이
 */
export async function aiResponseData(uid : string, docId : string)
 :Promise<Mistake> {
  const data = await getMistakeAnswerList(uid, docId);
  if (!Array.isArray(data) || data.length === 0) {
    throw new HttpsError("failed-precondition", "분석할 오답 데이터가 없습니다.");
  }

  const prompt = SYSTEM_PROMPT.replace("{USER_DATA}", JSON.stringify(data));

  const ai = getAiModel();
  const result = await ai.models.generateContent({
    model: "gemini-2.5-flash",
    contents: [{role: "user", parts: [{text: prompt}]}],
    config: {httpOptions: {timeout: 480000}},
  });
  const text = result.candidates?.[0]?.content?.parts?.[0]?.text;
  try {
    const jsonStr = (text ?? "").trim();
    // 응답이 순수 객체(JSON)로 오는 상황
    if (!jsonStr.startsWith("{")) {
      throw new HttpsError("internal", "AI 응답이 JSON 객체 형태가 아닙니다.");
    }
    const result = JSON.parse(jsonStr);
    return result as Mistake; // 3. as Study 타입 단언 제거
  } catch (error) {
    if (error instanceof HttpsError) throw error;
    throw new HttpsError("internal", "AI 응답 파싱에 실패했습니다.", error);
  }
}
