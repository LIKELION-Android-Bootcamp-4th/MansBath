import {logger} from "firebase-functions";
import {onRequest} from "firebase-functions/v2/https";
import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {Request, Response} from "express";
import {getAiStudyResponse} from "./Study/get_ai_response";
import {formatError} from "./Study/formetError";
import {authApp} from "./auth/auth";
import {question} from "./question/question";
import {generateRoadmap} from "./roadmap/roadmap";
import {loginWithNaver} from "./auth/naver";
import {makeQuiz} from "./quiz/quiz";

// Firebase Admin SDK 초기화
initializeApp();

export const addStudy = onRequest(
  {
    region: "asia-northeast3",
  },
  async (req: Request, res: Response): Promise<void> => {
    try {
      const study = await getAiStudyResponse();


      logger.log("AI 응답 성공", study);

      res.status(200).json({message: "AI 응답 성공", study});
    } catch (error : any) {
      logger.error("AI 응답 생성 실패", formatError(error));

      res.status(500).json({...formatError(error),
        message: "AI 응답 생성 실패"});
    }
  }
);

// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================
export const auth = onRequest({region: "asia-northeast3"}, authApp);
export const roadmap = onRequest({region: "asia-northeast3"}, generateRoadmap);

export {loginWithNaver, question, makeQuiz};
