import {HttpsError, onCall} from "firebase-functions/https";
import {saveMistakeAnalysis} from "./firestore_service";
import {logger} from "firebase-functions/v2";

/**
 * Mistake 분석을 요청하고 Firestore에 저장하는 메인 API 엔드포인트 (테스트용 onRequest)
 */
export const mistakeNotebook = onCall(async (req): Promise<any> => {
  const uid = (req.data?.uid as string) ?? req.auth?.uid ?? "test-user-for-web";
  const docId = req.data?.docId as string | undefined;
  if (!docId) {
    throw new HttpsError("invalid-argument", "docId is required");
  }

  try {
    const analysis = await saveMistakeAnalysis(uid, docId);

    logger.log("AI analysis 생성 및 저장 성공", analysis);
    return {mistake: analysis};
  } catch (error: any) {
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError("internal", "An internal error occurred.", error);
  }
});
