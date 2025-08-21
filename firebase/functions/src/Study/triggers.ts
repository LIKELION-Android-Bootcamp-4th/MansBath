import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {generateStudyContent} from "./gen_ai_service";
import {logger} from "firebase-functions";
import {getOrCreateStudy, saveStudy} from "./firestore_service";

export const studyTrigger = onDocumentCreated(
  {
    document: "users/{uid}/roadmap/{roadmapId}",
    timeoutSeconds: 300,
  },
  async (event) => {
    const data = event.data;

    logger.info("[TRIGGER TEST] 'studyTrigger' 함수가 실행되었습니다!");


    if (!data) {
      logger.log(`컬랙션 ${event.params.roadmapId} 이 삭제되거나 없습니다.`);
      return;
    }

    const roadmapData = data.data();
    const questionId = roadmapData.from;

    const uid = event.params.uid;
    const roadmapId = event.params.roadmapId;

    logger.log(`New roadmap created for user ${uid}. Generating studies for roadmap ${roadmapId}...`);

    const studyData = await generateStudyContent(uid, questionId, roadmapId);

    // Firestore 서비스 호출
    const {studyRef, existingStudy} = await getOrCreateStudy(uid);

    // Firestore에 저장
    await saveStudy(studyRef, studyData, existingStudy === null);

    logger.log("AI Study 생성 및 저장 성공", studyData);
    return {message: "AI 응답 성공", study: studyData};
  }
);
