import {logger} from "firebase-functions";
import {getAiModel} from "../ai/gen_ai";
import {SYSTEM_PROMPT} from "../ai/quiz_prompt";
import {ConceptDetail, Quiz} from "../type/quiz_types";
import {FieldValue} from "firebase-admin/firestore";
import {HttpsError} from "firebase-functions/v2/https";

/**
 * Gemini AI에게 퀴즈 생성을 요청하고 결과를 파싱합니다.
 * @param {ConceptDetail} conceptDetail - 퀴즈 생성 시 필요한 개념 상세 파일입니다.
 * @param {string} studyId - studyId
 * @return {Promise<Quiz>} 파싱된 AI 응답입니다.
 */
export async function getQuiz(
  conceptDetail: ConceptDetail,
  studyId: string,
): Promise<Quiz> {
  const model = getAiModel();
  const chat = model.startChat();
  const prompt = SYSTEM_PROMPT + JSON.stringify(conceptDetail);
  const result = await chat.sendMessage(prompt);
  const responseText = result.response.text();

  try {
    // 응답에서 JSON 객체만 추출
    const match = responseText.match(/{[\s\S]*}/);
    if (match) {
      return {
        ...JSON.parse(match[0]),
        studyId: studyId,
        createdAt: FieldValue.serverTimestamp(),
        status: false,
      } as Quiz;
    }
    // JSON 객체를 찾지 못한 경우
    throw new Error("No JSON object found in AI response");
  } catch (e) {
    logger.error("Failed to parse AI response.", {
      error: e,
      responseText: responseText,
    });
    // 파싱 실패 시, 에러 발생
    throw new HttpsError("invalid-argument", "Failed to parse AI response");
  }
}

//
