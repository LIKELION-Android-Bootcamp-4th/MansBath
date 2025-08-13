import {onRequest} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";

import {authApp} from "./auth/auth";
import {question} from "./question/question";
import {generateRoadmap} from "./roadmap/roadmap";
import {quizApp} from "./quiz/quiz";
import {loginWithNaver} from "./auth/naver";


// Firebase Admin SDK 초기화
initializeApp();

// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================
export const auth = onRequest({region: "asia-northeast3"}, authApp);
export const roadmap = onRequest({region: "asia-northeast3"}, generateRoadmap);
export const quiz = onRequest({region: "asia-northeast3"}, quizApp);

export {question};
export {loginWithNaver};
