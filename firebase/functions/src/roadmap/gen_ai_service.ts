import {getAiModel} from "../ai/gen_ai";
import {buildRoadmapPrompt} from "../ai/roadmap_prompt";
import {Roadmap} from "../type/roadmap_types";
import {formatFirestoreMapToString} from "../util/formatter";
import {cleanAndParseAiResponse} from "../util/parser";
import {getQuestionResult} from "./firestore_service";

/**
 * 사용자 질문 분석서를 토대로 AI를 통해 로드맵을 생성합니다.
 * @param {string} uid - 사용자 UID
 * @param {string} questionId - 질문 ID
 * @return {Promise<Roadmap>} 생성된 로드맵 데이터
 */
export async function generateRoadmapWithAI(
  uid: string,
  questionId: string
): Promise<Roadmap> {
  try {
    const ai = getAiModel();

    // 사용자 질문 분석서 조회
    const result = await getQuestionResult(uid, questionId);
    const userQuestionReport = formatFirestoreMapToString(result);

    // 로드맵 프롬프트 생성
    const fullPrompt = buildRoadmapPrompt(userQuestionReport);

    // 새로운 Google GenAI SDK를 사용한 콘텐츠 생성
    const response = await ai.models.generateContent({
      model: "gemini-2.5-flash",
      contents: [{role: "user", parts: [{text: fullPrompt}]}],
    });

    // 응답에서 텍스트 추출
    const rawAiOutput = response.text ?? "";

    // AI 응답 파싱
    const roadmap = cleanAndParseAiResponse<Roadmap>(rawAiOutput);

    return roadmap;
  } catch (err) {
    if (err instanceof Error) {
      console.error("AI 로드맵 생성 중 오류 발생:", err.message);
      throw new Error(err.message);
    } else {
      console.error("AI 로드맵 생성 중 알 수 없는 오류:", err);
      throw new Error("로드맵 생성 중 알 수 없는 오류가 발생했습니다.");
    }
  }
}
