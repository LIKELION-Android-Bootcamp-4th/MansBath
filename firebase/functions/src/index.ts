import {initializeApp} from "firebase-admin/app";

import {question} from "./question/question";
import {generateRoadmap} from "./roadmap/roadmap";
import {Study} from "./Study/study";
import {makeQuiz} from "./quiz/quiz";

import {loginWithNaver} from "./auth/naver";
import {checkFalseStatusScheduler /* , testCheckFalseStatus */} from "./fcm/fcm_service";

// Firebase Admin SDK 초기화
initializeApp();

// =================================================================
// ✨ 분리된 함수 내보내기
// =================================================================

export {question};
export {generateRoadmap};
export {Study};
export {makeQuiz};

export {loginWithNaver};
export {checkFalseStatusScheduler};

// TODO: 테스트 용, 배포시 주석 처리
// export {testCheckFalseStatus};
