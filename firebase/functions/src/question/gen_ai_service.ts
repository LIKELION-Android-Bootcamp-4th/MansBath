import {logger} from "firebase-functions";
import {model} from "../ai/gen_ai";
import {QUESTION_SYSTEM_PROMPT} from "../ai/question_prompt";
import {HistoryEntry, AiResponse} from "../type/question_types";

/**
 * Gemini에 전달할 대화 기록 배열을 생성합니다.
 * @param {HistoryEntry[]} history - 기존 대화 기록.
 * @return {object[]} Gemini API 형식에 맞는 대화 기록.
 */
function buildChatHistory(history: HistoryEntry[]) {
  const geminiHistory = history.map((entry) => {
    const content = typeof entry.message === "string" ?
      entry.message :
      JSON.stringify(entry.message);
    return {role: entry.role, parts: [{text: content}]};
  });

  // 시스템 프롬프트와 함께 전체 대화 기록 구성
  return [
    {role: "user" as const, parts: [{text: QUESTION_SYSTEM_PROMPT}]},
    {role: "model" as const, parts: [{text: "네, 알겠습니다."}]},
    ...geminiHistory,
  ];
}

/**
 * Gemini AI에게 응답을 요청하고 결과를 파싱합니다.
 * @param {HistoryEntry[]} history - 기존 대화 기록.
 * @param {string} userQuestion - 사용자의 새 질문.
 * @return {Promise<AiResponse>} 파싱된 AI 응답.
 */
export async function getAiResponse(
  history: HistoryEntry[],
  userQuestion: string,
): Promise<AiResponse> {
  const fullHistoryForAI = buildChatHistory(history);

  const chat = model.startChat({history: fullHistoryForAI});
  const result = await chat.sendMessage(userQuestion);
  const responseText = result.response.text();

  try {
    // 응답에서 JSON 객체만 추출
    const match = responseText.match(/{[\s\S]*}/);
    if (match) {
      return JSON.parse(match[0]) as AiResponse;
    }
    // JSON 객체를 찾지 못한 경우
    throw new Error("No JSON object found in AI response");
  } catch (e) {
    logger.error("Failed to parse AI response.", {
      error: e,
      responseText: responseText,
    });
    // 파싱 실패 시, 원본 텍스트를 메시지로 반환
    return {message: responseText};
  }
}
