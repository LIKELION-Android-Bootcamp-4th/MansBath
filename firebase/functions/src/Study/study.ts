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
    const {questionId, roadmapId, sectionId} = request.data as {
      questionId: string;
      roadmapId: string;
      sectionId : number;
    };

    const studyData = await generateStudyContent(uid, questionId,
      roadmapId, sectionId);
    const data = {...studyData, roadmapId};

    // Firestore 서비스 호출
    const {studyRef, existingStudy} = await getOrCreateStudy(uid);

    // Firestore에 저장
    await saveStudy(studyRef, data, existingStudy === null);

    logger.log("AI Study 생성 및 저장 성공", data);
    return {message: "AI 응답 성공", study: data};
  } catch (error) {
    logger.error("Study API 처리 중 오류 발생:", error);
    if (error instanceof HttpsError) {
      throw error;
    }
    throw new HttpsError("internal", "An internal error occurred.", error);
  }
});
