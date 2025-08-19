import {onCall, HttpsError} from "firebase-functions/v2/https";
import {logger} from "firebase-functions";
import {generateStudyContent} from "./gen_ai_service";
import {getOrCreateStudy, saveStudy} from "./firestore_service";

/**
 * Study 콘텐츠 생성을 요청하고 Firestore에 저장하는 메인 API 엔드포인트입니다.
 */
export const study = onCall(async (request) => {
  const uid = request.auth?.uid || "test-user-for-web";

  try {
    const {questionId, roadmapId} = request.data as {
      questionId: string;
      roadmapId: string;
    };

    const studyData = await generateStudyContent(uid, questionId, roadmapId);

    // 'any' 타입 객체의 title 속성에 접근
    const stepId = studyData.title;

    if (!stepId) {
      throw new HttpsError("internal", "AI 응답에 'title'이 없습니다.");
    }

    // Firestore 서비스 호출
    const {studyRef, existingStudy} = await getOrCreateStudy(uid, stepId);

    // Firestore에 저장
    await saveStudy(studyRef, studyData, existingStudy === null);

    logger.log("AI Study 생성 및 저장 성공", studyData);
    return {message: "AI 응답 성공", study: studyData};
  } catch (error) {
    logger.error("Study API 처리 중 오류 발생:", error);
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError("internal", "An internal error occurred.", error);
  }
});
