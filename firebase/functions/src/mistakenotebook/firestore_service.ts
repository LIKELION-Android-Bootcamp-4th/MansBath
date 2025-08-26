import {getFirestore} from "firebase-admin/firestore";
import {Mistake} from "../type/mistake";
import {HttpsError} from "firebase-functions/https";
import {aiResponseData} from "./gen_ai_service";

const USERS_COLLECTION = "users";
const MISTAKE_SUBCOLLECTION = "mistakeAnswer";

/**
 * Firestore에서 Study 문서를 가져오거나 새로 생성할 준비
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} docId - 문서 아이디
 */
export async function getMistakeAnswerList(
  uid: string, docId : string,
) {
  const docRef = getFirestore().doc(
    `${USERS_COLLECTION}/${uid}/${MISTAKE_SUBCOLLECTION}/${docId}`
  );
  const snap = await docRef.get();
  if (!snap.exists) {
    throw new Error(`문서를 찾을 수 없습니다: ${docRef.path}`);
  }
  const data = snap.data() || {};
  const items = Array.isArray((data as any).items) ? (data as any).items : [];
  return items;
}

/**
 * Firestore에서 Study 문서를 가져오거나 새로 생성할 준비
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} docId - 문서 아이디
 */
export async function saveMistakeAnalysis(uid : string, docId : string):
 Promise<Mistake> {
  const response = await aiResponseData(uid, docId);
  if (
    !response ||
    typeof response.root_cause !== "string" ||
    typeof response.evidence !== "string" ||
    typeof response.action_plan !== "string"
  ) {
    throw new HttpsError(
      "internal",
      "AI 분석 결과 형식이 올바르지 않습니다. (root_cause, evidence, action_plan 필요)"
    );
  }
  const db = getFirestore();
  const ref = db
    .collection(`${USERS_COLLECTION}/${uid}/${MISTAKE_SUBCOLLECTION}`)
    .doc(docId);
  await ref.set(
    {
      response,
    },
    {merge: true}
  );
  return response;
}
