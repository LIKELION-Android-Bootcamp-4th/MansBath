// study/gen_ai_service.ts
import {HttpsError} from "firebase-functions/v2/https";
import {SYSTEM_PROMPT} from "./prompt";
import {getAiModel} from "../ai/gen_ai";
import {fetchGenericFirebaseData} from "./firestore_service";

/**
 * AI를 호출하여 새로운 Study 콘텐츠를 생성
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} questionId - 콘텐츠 생성에 필요한 질문 ID.
 * @param {string} roadmapId - 콘텐츠 생성에 필요한 로드맵 ID.
 * @throws {HttpsError} AI 응답 파싱 실패 시 오류 발생.
 */
export async function generateStudyContent(
  uid: string,
  questionId: string,
  roadmapId: string,
): Promise<any> {
  const [userData, roadmapData] = await Promise.all([
    fetchGenericFirebaseData(uid, questionId, "questions", "result"),
    fetchGenericFirebaseData(uid, roadmapId, "roadmap"),
  ]);

  const prompt = SYSTEM_PROMPT.replace("{USER_DATA}", userData).replace(
    "{ROADMAP}",
    roadmapData,
  );

  const model = getAiModel();
  const result = await model.generateContent([prompt], {timeout: 480000});
  const text = result.response.candidates?.[0]?.content?.parts?.[0]?.text;

  try {
    const match = text?.match(/\[([\s\S]*)]/);
    if (!match?.[0]) {
      throw new HttpsError("internal", "AI 응답에서 JSON 배열을 찾지 못했습니다.");
    }
    const raw = JSON.parse(match[0]);
    return raw[0]; // 3. as Study 타입 단언 제거
  } catch (error) {
    if (error instanceof HttpsError) throw error;
    throw new HttpsError("internal", "AI 응답 파싱에 실패했습니다.", error);
  }
}
