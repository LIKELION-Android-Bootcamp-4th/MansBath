import {
  getFirestore,
  DocumentReference,
  FieldValue,
} from "firebase-admin/firestore";
import {logger} from "firebase-functions";
import {formatError} from "./formetError";

const USERS_COLLECTION = "users";
const STUDIES_SUBCOLLECTION = "studies";

/**
 * Firestore에서 Study 문서를 가져오거나 생성할 준비
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} stepId - Study 문서의 ID (예: 제목).
 * @throws {Error} Firestore 작업 중 오류 발생 시.
 */
export async function getOrCreateStudy(
  uid: string,
  stepId: string,
): Promise<{
  studyRef: DocumentReference;
  existingStudy: any | null; // 2. Study -> any로 변경
}> {
  const studyRef = getFirestore()
    .collection(`${USERS_COLLECTION}/${uid}/${STUDIES_SUBCOLLECTION}`)
    .doc(stepId);

  const docSnap = await studyRef.get();

  const existingStudy = docSnap.exists ? docSnap.data() : null;

  return {studyRef, existingStudy};
}

/**
 * Study 데이터를 Firestore 문서에 저장하거나 업데이트
 * @param {DocumentReference} studyRef - 저장할 Firestore 문서의 참조.
 * @param {Record<string, any>} data - 저장할 데이터 객체.
 * @param {boolean} isNew - 새로 생성된 문서인지 여부.
 * @throws {Error} Firestore 저장 중 오류 발생 시.
 */
export async function saveStudy(
  studyRef: DocumentReference,
  data: Record<string, any>,
  isNew: boolean,
): Promise<void> {
  const dataToSave = {
    ...data,
    lastUpdatedAt: FieldValue.serverTimestamp(),
  };

  if (isNew) {
    (dataToSave as any).createdAt = FieldValue.serverTimestamp();
  }

  try {
    await studyRef.set(dataToSave, {merge: true});
    logger.log(`Study 데이터 저장 완료: ${studyRef.path}`);
  } catch (error) {
    logger.error(
      `Study 데이터 저장 중 에러 (경로: ${studyRef.path})`,
      formatError(error),
    );
    throw error;
  }
}

/**
 * Firestore에서 특정 문서를 범용적으로 조회
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} docId - 조회할 문서의 ID.
 * @param {string} subCollection - 문서가 위치한 하위 컬렉션의 이름.
 * @param {string | undefined} fieldName - 조회할 특정 필드의 이름 (선택 사항).
 * @return {Promise<string>} 필드 값 또는 문서 전체 데이터의 JSON 문자열.
 */
export async function fetchGenericFirebaseData(
  uid: string,
  docId: string,
  subCollection: string,
  fieldName?: string,
): Promise<string> {
  const docRef = getFirestore().doc(`${USERS_COLLECTION}/${uid}/${subCollection}/${docId}`);
  const docSnap = await docRef.get();
  if (!docSnap.exists) {
    throw new Error(`문서를 찾을 수 없습니다: ${docRef.path}`);
  }
  const data = docSnap.data();
  if (!data) return "{}";

  if (fieldName) {
    const result = data[fieldName];
    if (typeof result === "undefined") {
      throw new Error(`"${fieldName}" 필드를 찾을 수 없습니다.`);
    }
    return typeof result === "string" ? result : JSON.stringify(result);
  }
  return JSON.stringify(data);
}
