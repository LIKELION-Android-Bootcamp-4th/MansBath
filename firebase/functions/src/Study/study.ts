import {onCall, HttpsError} from "firebase-functions/v2/https";
import {logger} from "firebase-functions";
import {getAiStudyResponse} from "./get_ai_response";
import {formatError} from "./formetError";

export const Study = onCall(
  {
    region: "asia-northeast3",
  },
  async () => {
    try {
      const study = await getAiStudyResponse();
      logger.log("AI 응답 성공", study);
      return {message: "AI 응답 성공", study};
    } catch (error : any) {
      logger.error("AI 응답 생성 실패", formatError(error));
      throw new HttpsError("internal", formatError(error).message || "AI 응답 생성 실패");
    }
  }
);
