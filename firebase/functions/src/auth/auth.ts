import express from "express";
import cors from "cors";
import {loginWithGoogle} from "./google";

const app = express();
app.use(cors({origin: true}));
app.use(express.json());

app.post("/google", loginWithGoogle);
// app.post("/naver", loginWithNaver);

/**
 * 이 앱은 SNS 로그인 요청을 처리하는 엔트리 포인트입니다.
 *
 * @remarks
 * 내부 라우트에 따라 호출을 분기하며,
 * 정의되지 않은 경로(예: 루트 경로 "/")로 요청이 들어오면
 * Express의 기본 동작에 따라 404 Not Found 응답을 반환합니다.
 */
export const authApp = app;
