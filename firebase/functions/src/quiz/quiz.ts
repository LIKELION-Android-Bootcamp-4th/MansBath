import {logger} from "firebase-functions";
import {onCall, HttpsError} from "firebase-functions/v2/https";
import {getQuiz} from "./gen_ai_service";
import {getConceptDetail, saveQuiz} from "./firestore_service";

/**
 * @summary Firebase Callable Function: 사용자 질문 처리 및 AI 응답 반환
 */
export const makeQuiz = onCall(async (request) => {
  // 1. 사용자 식별 정보 (UID)
  const uid = request.auth?.uid || "test-user-for-web";

  try {
    // 2. 요청 데이터
    const {
      roadmapId, studyId,
    } = request.data as {
      roadmapId?: string,
      studyId?: string; };

    if (!roadmapId) {
      throw new HttpsError("invalid-argument", "roadmapId is required.");
    }

    if (!studyId) {
      throw new HttpsError("invalid-argument", "studyId is required.");
    }

    // 3. 개념 상세 데이터 조회 (Firestore Service)
    const conceptDetail = await getConceptDetail(uid, studyId);

    // 4. AI 응답 요청 (Gemini Service)
    const aiResponse = await getQuiz(conceptDetail, studyId);

    // 5. 퀴즈 내용 저장 (Firestore Service)
    await saveQuiz(uid, aiResponse, roadmapId);

    // 6. 응답 반환
    return {
      ...aiResponse,
      createdAt: new Date().toISOString(),
    };
  } catch (error) {
    logger.error("API 처리 중 오류 발생:", error);
    throw new HttpsError("internal", "An internal error occurred.");
  }
});
