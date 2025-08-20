import {onCall, HttpsError} from "firebase-functions/v2/https";
import {logger} from "firebase-functions";
import {
  getOrCreateQuestion,
  saveConversation,
} from "./firestore_service";
import {getAiResponse} from "./gen_ai_service";

/**
 * @summary 사용자 질문을 처리하고 AI 응답을 반환하는 메인 API 엔드포인트.
 * @description onCall 방식으로 변환함. 테스트 필요함.
 * @route POST /
 */
export const question = onCall(async (request) => {
  // 1. 사용자 식별 정보 (UID)
  const uid = request.auth?.uid || "test-user-for-web";

  try {
    const {
      questionId,
      question: userQuestion,
    } = request.data as { questionId?: string; question: string };

    if (!userQuestion) {
      throw new HttpsError("invalid-argument", "The 'question' field is required.");
    }

    const {questionRef, history, questionId: newQuestionId} =
      await getOrCreateQuestion(uid, questionId);

    const aiResponse = await getAiResponse(history, userQuestion);

    await saveConversation(questionRef, {history, userQuestion, aiResponse});

    return {
      ...aiResponse,
      questionId: newQuestionId,
    };
  } catch (error) {
    logger.error("API 처리 중 오류 발생:", error);
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError("internal", "An internal error occurred.", error);
  }
});
