import {model} from "../ai/gen_ai";
import {buildRoadmapPrompt} from "../ai/roadmap_prompt";
import {Roadmap} from "../type/roadmap_types";
import {formatFirestoreMapToString} from "../util/formatter";
import {cleanAndParseAiResponse} from "../util/parser";
import {validateBody} from "./validator";
import {getQuestionResult, saveRoadmap} from "./firestore_service";
import {onCall} from "firebase-functions/v2/https";

/**
 * 사용자 질문 분석서를 토대로 AI를 통해 로드맵을 생성합니다.
 *
 * @param {Request} req
 * @param {Response} res
 */
export const generateRoadmap = onCall({region: "asia-northeast3"}, async (request) => {
  try {
    const {uid, questionId} = validateBody(request.data);

    const result = await getQuestionResult(uid, questionId);

    const userQuestionReport = formatFirestoreMapToString(result);

    const fullPrompt = buildRoadmapPrompt(userQuestionReport);

    const response = await model.generateContent({
      contents: [{role: "user", parts: [{text: fullPrompt}]}],
    });
    const rawAiOutput = response.response.text();

    const roadmap = cleanAndParseAiResponse<Roadmap>(rawAiOutput);

    const roadmapRefId = await saveRoadmap(uid, roadmap);

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
