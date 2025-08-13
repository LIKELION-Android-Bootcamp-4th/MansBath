import {onRequest} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";
import {question} from "./question/question";
import {generateRoadmap} from "./roadmap/roadmap";
import {loginWithNaver} from "./auth/naver";
import {makeQuiz} from "./quiz/quiz";
import {Study} from "./Study/study";

// Firebase Admin SDK 초기화
initializeApp();

// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================
export const roadmap = onRequest({region: "asia-northeast3"}, generateRoadmap);

export {loginWithNaver, question, makeQuiz, Study};