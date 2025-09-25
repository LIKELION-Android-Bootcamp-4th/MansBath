// study/gen_ai_service.ts
import {HttpsError} from "firebase-functions/v2/https";
import {SYSTEM_PROMPT} from "./prompt";
import {getAiModel} from "../ai/gen_ai";
import {fetchGenericFirebaseData} from "./firestore_service";
import {logger} from "firebase-functions/v2";

/**
 * AI를 호출하여 새로운 Study 콘텐츠를 생성
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} questionId - 콘텐츠 생성에 필요한 질문 ID.
 * @param {string} roadmapId - 콘텐츠 생성에 필요한 로드맵 ID.
 * @param {number} sectionId - 콘텐츠 생성에 필요한 로드맵 ID.
 * @throws {HttpsError} AI 응답 파싱 실패 시 오류 발생.
 */
export async function generateStudyContent(
  uid: string,
  questionId: string,
  roadmapId: string,
  sectionId : number
): Promise<any> {
  const [userData, roadmapData] = await Promise.all([
    fetchGenericFirebaseData(uid, questionId, "questions", "result"),
    fetchGenericFirebaseData(uid, roadmapId, "roadmap", "stages", sectionId),
  ]);

  const prompt = SYSTEM_PROMPT.replace("{USER_DATA}", userData).replace(
    "{ROADMAP}",
    roadmapData,
  );

  const ai = getAiModel();
  const result = await ai.models.generateContent({
    model: "gemini-2.5-flash",
    contents: [{role: "user", parts: [{text: prompt}]}],
    config: {httpOptions: {timeout: 480000}},
  });
  const parts = result.candidates?.[0]?.content?.parts ?? [];
  const full = parts
    .map((p: { text?: string } | null | undefined) => (typeof p?.text === "string" ? p.text : ""))
    .join("");
  logger.log(result);
  try {
    const bodyMatch = full.match(/```(?:json)?\s*([\s\S]*?)\s*```/i);
    const body = (bodyMatch?.[1] ?? full).trim();
    const start = body.search(/[{[]/);
    if (start < 0) {
      throw new HttpsError("internal", "AI 응답에서 JSON을 찾지 못했습니다.");
    }
    const parsed = JSON.parse(body.slice(start));
    return Array.isArray(parsed) ? parsed[0] : parsed;
  } catch (error) {
    if (error instanceof HttpsError) throw error;
    throw new HttpsError("internal", "AI 응답 파싱에 실패했습니다.", error);
  }
}
