import {onRequest} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";

import {authApp} from "./auth";
import {questionApp} from "./question/question";

// Firebase Admin SDK 초기화
initializeApp();

// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================
export const auth = onRequest({region: "asia-northeast3"}, authApp);
export const question = onRequest({region: "asia-northeast3"}, questionApp);
