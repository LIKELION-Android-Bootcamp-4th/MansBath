import {HttpsError, onRequest} from "firebase-functions/https";
import {saveMistakeAnalysis} from "./firestore_service";
import {logger} from "firebase-functions/v2";

/**
 * Mistake 분석을 요청하고 Firestore에 저장하는 메인 API 엔드포인트 (테스트용 onRequest)
 */
export const mistakeNotebook = onRequest(async (req, res): Promise<void> =>{
  const uid = req.body.uid || "test-user-for-web";
  const docId = req.body.docId || "Akjo3w9uzNrYOTdiFq4D";
  if (!docId) {
    res.status(400).send({message: "문서가 비었습니다."});
    return;
  }

  try {
    const analysis = await saveMistakeAnalysis(uid, docId);

    logger.log("AI analysis 생성 및 저장 성공", analysis);
    res.status(200).json(analysis);
    return;
  } catch (error : any) {
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError("internal", "An internal error occurred.", error);
  }
});
