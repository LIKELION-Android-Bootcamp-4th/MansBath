import {onRequest} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";
import {question} from "./question/question";
import {quizApp} from "./quiz/quiz";
import {generateRoadmap} from "./roadmap/roadmap";
import {loginWithNaver} from "./auth/naver";
import {Study} from "./Study/study";

// Firebase Admin SDK 초기화
initializeApp();


// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================
export const roadmap = onRequest({region: "asia-northeast3"}, generateRoadmap);
export const quiz = onRequest({region: "asia-northeast3"}, quizApp);
export {Study};
export {loginWithNaver, question};
