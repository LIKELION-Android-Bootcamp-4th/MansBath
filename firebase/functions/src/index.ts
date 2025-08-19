import {initializeApp} from "firebase-admin/app";
import {setGlobalOptions} from "firebase-functions/v2";

// Initialization
setGlobalOptions({region: "asia-northeast3"});
initializeApp();


// Functions
export * from "./question/question";
export * from "./roadmap/roadmap";
export * from "./quiz/quiz";
export * from "./auth/naver";

// FIX
// export * from "./Study/study";
