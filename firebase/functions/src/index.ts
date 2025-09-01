import * as admin from "firebase-admin";
import {setGlobalOptions} from "firebase-functions/v2";

import {checkFalseStatusScheduler /* , testCheckFalseStatus */} from "./fcm/fcm_service";

// Initialization
setGlobalOptions(
  {
    region: "asia-northeast3",
    invoker: "public",
  }
);

if (admin.apps.length === 0) {
  admin.initializeApp();
}

// Functions
export * from "./question/question";
export * from "./roadmap/roadmap";
export * from "./quiz/quiz";
export * from "./auth/naver";
export * from "./auth/withdraw";
export * from "./Study/study";
export * from "./mistakenotebook/mistake_notebook";

// Triggers
export {studyTrigger} from "./Study/triggers";
export {quizTrigger} from "./quiz/triggers";

export {checkFalseStatusScheduler};

// TODO: 테스트 용, 배포시 주석 처리
// export {testCheckFalseStatus};
