import {SYSTEM_PROMPT} from "./dummy_data";
import {model} from "./config";
import {logger} from "firebase-functions";
import {saveStudyToFirestore} from "./firestore";
import {Study} from "./study_entity";
import {formatError} from "./formetError";
import {fetchFirebaseData} from "./fetchfirebase_data";

/**
 * Gemini 모델에 프롬프트를 보내고 개념 상세 응답(JSON)을 생성합니다.
 *
 * @return {Promise<Study>} 개념 상세 파일 객체(JSON 파싱된 결과)
 * @throws {Error} AI 응답 생성 또는 파싱 실패 시 오류 발생
 */
export async function getAiStudyResponse(): Promise<Study> {
  const uid = "test-user-for-web";
  const userId = "wYN1b3dA0kGffMXCcv73";
  const questions = "questions";
  const userFieldName = "result";

  const roadId = "RlLO7d2m1uVFgqH42wsu";
  const roadmap = "roadmap";

  const [userData, roadmapData] = await Promise.all([
    fetchFirebaseData(uid, userId, questions, userFieldName),
    fetchFirebaseData(uid, roadId, roadmap),
  ]);

  const prompt = SYSTEM_PROMPT
    .replace("{USER_DATA}", userData)
    .replace("{ROADMAP}", roadmapData);

  const result = await model.generateContent([prompt], {
    timeout: 480000, // 응답 시간 480초 설정
  });

  const text = result.response.candidates?.[0]?.content?.parts?.[0]?.text;

  try {
    const match = text?.match(/\[([\s\S]*)\]/);
    if (!match) {
      throw new Error("JSON 배열이 응답에 없음");
    }

    const raw = JSON.parse(match[0]);
    const data = raw[0];

    await saveStudyToFirestore(data);
    return data;
  } catch (error: unknown) {
    logger.error("AI 응답 파싱 오류", formatError(error));
    throw new Error("AI 생성에 실패하였습니다.");
  }
}
