import {onCall} from "firebase-functions/v2/https";
import {validateBody} from "../util/uid_validator";
import {saveRoadmap} from "./firestore_service";
import {generateRoadmapWithAI} from "./gen_ai_service";

/**
 * 사용자 질문 분석서를 토대로 AI를 통해 로드맵을 생성합니다.
 *
 * @param {Request} req
 * @param {Response} res
 */
export const generateRoadmap = onCall(async (request) => {
  try {
    const {uid, questionId} = validateBody(request.data);

    // AI를 통해 로드맵 생성
    const roadmap = await generateRoadmapWithAI(uid, questionId);

    // 로드맵을 Firestore에 저장
    const roadmapRefId = await saveRoadmap(uid, questionId, roadmap);

    console.log(`roadmapRefId: ${roadmapRefId}`); // 예: "4z8QJXyBcM7n2Jgf1ZpA"

    return {
      message: "로드맵이 저장되었습니다.",
      docId: roadmapRefId,
      data: roadmap,
    };
  } catch (err) {
    if (err instanceof Error) {
      console.error("오류 발생:", err.message);
      throw new Error(err.message);
    } else {
      console.error("오류 발생:", err);
      throw new Error("알 수 없는 오류가 발생했습니다.");
    }
  }
});
