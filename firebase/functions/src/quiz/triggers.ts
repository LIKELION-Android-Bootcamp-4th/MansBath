import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {getQuiz} from "./gen_ai_service";
import {getConceptDetail, saveQuiz} from "./firestore_service";
import {logger} from "firebase-functions";

export const quizTrigger = onDocumentCreated(
  {
    document: "users/{uid}/studies/{studyId}",
    timeoutSeconds: 300,
  },
  async (event) => {
    const data = event.data;

    logger.info("[TRIGGER TEST] 'quizTrigger' 함수가 실행되었습니다!");


    if (!data) {
      logger.log(`컬랙션 ${event.params.studyId} 이 삭제되거나 없습니다.`);
      return;
    }

    const uid = event.params.uid;
    const studyId = event.params.studyId;

    // 3. 개념 상세 데이터 조회 (Firestore Service)
    const conceptDetail = await getConceptDetail(uid, studyId);

    // 4. AI 응답 요청 (Gemini Service)
    const aiResponse = await getQuiz(conceptDetail, studyId);

    // 5. 퀴즈 내용 저장 (Firestore Service)
    await saveQuiz(uid, aiResponse);

    // 6. 응답 반환
    return {
      ...aiResponse,
      createdAt: new Date().toISOString(),
    };
  }
);
