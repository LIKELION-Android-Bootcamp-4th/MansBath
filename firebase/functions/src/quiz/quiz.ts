import {getAuth} from "firebase-admin/auth";
import express, {Request, Response} from "express";
import cors from "cors";
import {logger} from "firebase-functions";
import {getQuiz} from "./gen_ai_service";
import {getConceptDetail, saveQuiz} from "./firebasse_service";

const app = express();
app.use(cors({origin: true}));
app.use(express.json());

/**
 * @summary 사용자 질문을 처리하고 AI 응답을 반환하는 메인 API 엔드포인트.
 * @description 사용자 인증, 대화 기록 조회/생성, AI 응답 요청, 대화 내용 저장을 순차적으로 처리합니다.
 * @route POST /
 */
app.post("/", async (req: Request, res: Response) => {
  try {
    // 1. 사용자 인증
    const idToken = req.headers.authorization?.split("Bearer ")[1];
    let uid = "test-user-for-web"; // 웹 테스트용 기본 UID
    if (idToken) {
      uid = (await getAuth().verifyIdToken(idToken)).uid;
      logger.log(uid);
    }

    const quizName = req.body.quizName as string;

    // 2. 개념 상세 데이터 조회 (Firestore Service)
    const conceptDetail = await getConceptDetail(uid, quizName);

    // 3. AI 응답 요청 (Gemini Service)
    const aiResponse = await getQuiz(conceptDetail);

    // 4. 퀴즈 내용 저장 (Firestore Service)
    await saveQuiz(uid, aiResponse);

    // 5. 클라이언트에 응답
    const responseToClient = {
      ...aiResponse,
      createdAt: new Date().toISOString(),
    };
    return res.status(200).json(responseToClient);
  } catch (error) {
    logger.error("API 처리 중 오류 발생:", error);
    return res.status(500).json({error: "An internal error occurred."});
  }
});

export const quizApp = app;
