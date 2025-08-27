import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {logger} from "firebase-functions";
import {generateStudyContent} from "./gen_ai_service";
import {getOrCreateStudy, saveStudy} from "./firestore_service";

interface SuccessfulResult {
  roadmapId: string;
  sectionId: number | string;
  [key: string]: any;
}

interface FailedSectionInfo {
  error: true;
  sectionId: number | string;
  reason: string;
}

export const studyTrigger = onDocumentCreated(
  {
    document: "users/{uid}/roadmap/{roadmapId}",
    timeoutSeconds: 540,
  },
  async (event) => {
    const {uid, roadmapId} = event.params;
    logger.info(`[START] studyTrigger 실행. uid: ${uid}, roadmapId: ${roadmapId}`);

    const roadmapSnapshot = event.data;

    if (!roadmapSnapshot) {
      logger.warn(`[ABORT] 문서가 없습니다: users/${uid}/roadmap/${roadmapId}`);
      return;
    }

    const roadmapData = roadmapSnapshot.data();
    const roadmapField = roadmapData.roadmap;
    const questionId = roadmapData.questionId;
    const stages = roadmapField?.stages;

    if (!questionId || !Array.isArray(stages) || stages.length === 0) {
      logger.error("[ABORT] 필수 데이터('questionId', 'stages')를 찾지 못했습니다.", {roadmapId, uid, data: roadmapData});
      return;
    }

    logger.info(`[DATA] questionId: ${questionId}, 총 ${stages.length}개의 섹션 처리 시작.`);

    try {
      const generationPromises = stages.map((stage) => {
        const sectionId = stage.id;
        if (sectionId === undefined) return Promise.resolve(null);

        logger.log(`[PROCESS] Section #${sectionId} 콘텐츠 생성 시작...`);

        return generateStudyContent(uid, questionId, roadmapId, sectionId)
          .then((studyData) => ({...studyData, roadmapId, sectionId} as SuccessfulResult))
          .catch((error) => {
            return {error: true, sectionId: sectionId, reason: error.message} as FailedSectionInfo;
          });
      });

      const outcomes = await Promise.all(generationPromises);

      const successfulResults: SuccessfulResult[] = [];
      const failedSections: FailedSectionInfo[] = [];

      outcomes.forEach((outcome) => {
        if (outcome) {
          if ("error" in outcome) {
            failedSections.push(outcome as FailedSectionInfo);
          } else {
            successfulResults.push(outcome as SuccessfulResult);
          }
        }
      });

      logger.info(`[SUMMARY] 처리 완료: 성공 ${successfulResults.length}건, 실패 ${failedSections.length}건`);

      if (failedSections.length > 0) {
        failedSections.forEach((failure) => {
          logger.error(`[FAILURE] Section #${failure.sectionId} 생성 실패 (Roadmap: ${roadmapId}). 원인:`, failure.reason);
        });
      }

      if (successfulResults.length > 0) {
        logger.info(`[DB-WRITE] ${successfulResults.length}개의 study 문서를 개별적으로 저장 시작.`);

        const savePromises = successfulResults.map(async (dataToSave) => {
          const {studyRef, existingStudy} = await getOrCreateStudy(uid);

          await saveStudy(studyRef, dataToSave, existingStudy === null);
        });

        await Promise.all(savePromises);

        logger.info(`[SUCCESS] 성공적으로 ${successfulResults.length}개의 Study 문서를 'studies' 컬렉션에 저장했습니다.`);
      } else {
        logger.warn("[DB-WRITE] 저장할 성공적인 결과가 없습니다.");
      }

      return {message: `처리 완료: ${successfulResults.length}건 성공, ${failedSections.length}건 실패`};
    } catch (error) {
      logger.error(`[FATAL] '${roadmapId}' 로드맵 처리 중 심각한 오류 발생:`, error);
      throw error;
    }
  }
);
