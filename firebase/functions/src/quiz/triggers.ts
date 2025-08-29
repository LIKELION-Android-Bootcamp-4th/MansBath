import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {logger} from "firebase-functions";
import {getQuiz} from "./gen_ai_service";
import {getConceptDetail, saveQuiz} from "./firestore_service";

export const quizTrigger = onDocumentCreated(
  {
    document: "users/{uid}/studies/{studyId}",
    timeoutSeconds: 300,
  },
  async (event) => {
    logger.info("[TRIGGER] 'quizTrigger'가 실행되었습니다!");

    const studySnapshot = event.data;

    if (!studySnapshot) {
      logger.log(`Study 문서(${event.params.studyId})가 삭제되었거나 없습니다.`);
      return;
    }

    const {uid, studyId} = event.params;
    const studyData = studySnapshot.data();

    const sectionId = studyData.sectionId;
    const roadmapId = studyData.roadmapId;

    if (sectionId === undefined || !roadmapId) {
      logger.error(`Study 문서(${studyId})에 'sectionId' 또는 'roadmapId' 필드가 없습니다.`);
      return;
    }

    try {
      const conceptDetail = await getConceptDetail(uid, studyId);

      logger.log(`퀴즈 생성 시작: sectionId=${sectionId}, roadmapId=${roadmapId}`);

      const aiResponse = await getQuiz(conceptDetail, studyId, Number(sectionId));

      await saveQuiz(uid, aiResponse, roadmapId);

      logger.log(`✅ 퀴즈 생성 및 저장 성공: studyId=${studyId}`);
      return {
        ...aiResponse,
        createdAt: new Date().toISOString(),
      };
    } catch (error) {
      logger.error("퀴즈 트리거 처리 중 오류 발생:", {studyId, sectionId, error});
      throw error;
    }
  }
);
