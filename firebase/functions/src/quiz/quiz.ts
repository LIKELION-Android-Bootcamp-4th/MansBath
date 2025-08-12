import {logger} from "firebase-functions";
import {onCall, HttpsError} from "firebase-functions/v2/https";
import {getQuiz} from "./gen_ai_service";
import {getConceptDetail, saveQuiz} from "./firebasse_service";

/**
 * @summary Firebase Callable Function: 사용자 질문 처리 및 AI 응답 반환
 */
export const makeQuiz = onCall(
  {region: "asia-northeast3"},
  async (request) => {
    try {
    // 1. 사용자 인증
      const uid = "test-user-for-web"; // 기본 UID (테스트용)
      // request.auth?.uid ??
      const quizName = request.data.quizName as string;
      if (!quizName) {
        throw new HttpsError(
          "invalid-argument",
          "quizName is required."
        );
      }

      // 2. 개념 상세 데이터 조회 (Firestore Service)
      const conceptDetail = await getConceptDetail(uid, quizName);

      // 3. AI 응답 요청 (Gemini Service)
      const aiResponse = await getQuiz(conceptDetail);

      // 4. 퀴즈 내용 저장 (Firestore Service)
      await saveQuiz(uid, aiResponse);

      // 5. 응답 반환
      return {
        ...aiResponse,
        createdAt: new Date().toISOString(),
      };
    } catch (error) {
      logger.error("API 처리 중 오류 발생:", error);
      throw new HttpsError(
        "internal",
        "An internal error occurred."
      );
    }
  });
