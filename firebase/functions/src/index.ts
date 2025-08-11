import {logger} from "firebase-functions";
import {onRequest} from "firebase-functions/v2/https";
import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {Request, Response} from "express";
import {getAiStudyResponse} from "./Study/get_ai_response";
import {formatError} from "./Study/formetError";

import {authApp} from "./auth/auth";
import {questionApp} from "./question/question";
import {quizApp} from "./quiz/quiz";
import {generateRoadmap} from "./roadmap/roadmap";

initializeApp();


export const addmessage = onRequest({
  region: "asia-northeast3",
}, async (req: Request, res: Response) => {
  const original = req.query.text;
  const writeResult = await getFirestore()
    .collection("messages")
    .add({original: original});

  res.json({result: `Message with ID: ${writeResult.id} added.`});
});


export const makeuppercase = onDocumentCreated(
  {
    region: "asia-northeast3",
    document: "/messages/{documentId}",
  },
  (event) => {
    const original = event.data?.data().original;

    logger.log("Uppercasing", event.params.documentId, original);

    const uppercase = original.toUpperCase();

    return event.data?.ref.set({uppercase}, {merge: true});
  });


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

export const auth = onRequest({region: "asia-northeast3"}, authApp);
export const question = onRequest({region: "asia-northeast3"}, questionApp);
export const quiz = onRequest({region: "asia-northeast3"}, quizApp);
export const roadmap = onRequest({region: "asia-northeast3"}, generateRoadmap);