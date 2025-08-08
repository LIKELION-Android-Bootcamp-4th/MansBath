import {logger} from "firebase-functions";
import {model, SYSTEM_PROMPT} from "./gen_ai";
import {ConceptDetail, Quiz} from "./types";

/**
 * Gemini AI에게 퀴즈 생성을 요청하고 결과를 파싱합니다.
 * @param {ConceptDetail} conceptDetail - 퀴즈 생성 시 필요한 개념 상세 파일입니다.
 * @return {Promise<Quiz>} 파싱된 AI 응답입니다.
 */
export async function getQuiz(
  conceptDetail: ConceptDetail | string
): Promise<Quiz> {
  const chat = model.startChat();
  const prompt = SYSTEM_PROMPT + JSON.stringify(conceptDetail);
  const result = await chat.sendMessage(prompt);
  const responseText = result.response.text();

  try {
    // 응답에서 JSON 객체만 추출
    const match = responseText.match(/{[\s\S]*}/);
    if (match) {
      return JSON.parse(match[0]) as Quiz;
    }
    // JSON 객체를 찾지 못한 경우
    throw new Error("No JSON object found in AI response");
  } catch (e) {
    logger.error("Failed to parse AI response.", {
      error: e,
      responseText: responseText,
    });
    // 파싱 실패 시, 더미 데이터를 메시지로 반환
    return {
      quizTitle: responseText,
      questions: [
        {
          question: "더미 퀴즈",
          options: [
            "Visual Studio Code",
            "Xcode",
            "Android Studio",
            "Eclipse",
          ],
          answer: "Android Studio",
          explanation: "Android Studio는 구글에서 제공하는 안드로이드 앱.",
        },
      ],
    };
  }
}

//
