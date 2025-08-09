import {logger} from "firebase-functions";
import {formatError} from "./formetError";
import {Study} from "./study_entity";
import * as admin from "firebase-admin";
/**
 * Study 객체를 Firestore의 "studies" 컬렉션에 저장합니다.
 * 문서 ID는 duration 값을 기반으로 생성됩니다. (예: "Step 1" → "step1")
 *
 * @param {Study} study - 저장할 Study 객체
 * @return {Promise<void>} 저장 완료 후 반환되는 Promise
 */
export async function saveStudyToFirestore(study: Study): Promise<void> {
  const stepId = study.title;
  const uid = "test-user-for-web";
  const database = admin.firestore();

  try {
    await database.collection(`users/${uid}/studies`).doc(stepId).set(study);

    logger.log(
      `저장 완료: users/${uid}/studies/${stepId}`
    );
  } catch (error) {
    logger.error("저장 중 에러", formatError(error));
  }
}
