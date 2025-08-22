import {
  getFirestore,
  DocumentReference,
  FieldValue,
} from "firebase-admin/firestore";
import {logger} from "firebase-functions";
import {formatError} from "../util/formetError";

const USERS_COLLECTION = "users";
const STUDIES_SUBCOLLECTION = "studies";

/**
 * Firestore에서 Study 문서를 가져오거나 새로 생성할 준비
 * @param {string} uid - 대상 사용자의 ID.
 * @throws {Error} Firestore 작업 중 오류 발생 시.
 */
export async function getOrCreateStudy(
  uid: string,
): Promise<{
  studyRef: DocumentReference;
  existingStudy: any | null;
  studyId: string; // 확정된 studyId를 반환합니다.
}> {
  const collectionRef = getFirestore()
    .collection(`${USERS_COLLECTION}/${uid}/${STUDIES_SUBCOLLECTION}`);

  let studyRef: DocumentReference;
  let existingStudy: any | null = null;
  let newStepId;

  if (newStepId) {
    // stepId가 제공된 경우 (기존 문서 조회)
    studyRef = collectionRef.doc(newStepId);
    const docSnap = await studyRef.get();
    if (docSnap.exists) {
      existingStudy = docSnap.data() ?? null;
    }
  } else {
    // stepId가 없는 경우 (새 문서 생성)
    studyRef = collectionRef.doc();
    newStepId = studyRef.id;
  }

  return {studyRef, existingStudy, studyId: newStepId};
}

/**
 * Study 데이터를 Firestore 문서에 저장하거나 업데이트
 * @param {DocumentReference} studyRef - 저장할 Firestore 문서의 참조.
 * @param {Record<string, any>} data - 저장할 데이터 객체.
 * @param {any | null} existingStudy - 기존 Study 데이터. null이면 새 문서로 간주.
 * @throws {Error} Firestore 저장 중 오류 발생 시.
 */
export async function saveStudy(
  studyRef: DocumentReference,
  data: Record<string, any>,
  existingStudy: any | null,
): Promise<void> {
  const dataToSave = {
    ...data,
    lastUpdatedAt: FieldValue.serverTimestamp(),
  };

  // existingStudy가 null이면 새로운 문서이므로 createdAt을 추가합니다.
  if (!existingStudy) {
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
 * Firestore에서 특정 문서를 범용적으로 조회 (이 함수는 변경되지 않았습니다)
 * @param {string} uid - 대상 사용자의 ID.
 * @param {string} docId - 조회할 문서의 ID.
 * @param {string} subCollection - 문서가 위치한 하위 컬렉션의 이름.
 * @param {string} fieldName - 조회할 특정 필드의 이름 (선택 사항).
 * @param {number} index - 스테이지 넘버 확인
 * @return {Promise<string>} 필드 값 또는 문서 전체 데이터의 JSON 문자열.
 */
export async function fetchGenericFirebaseData(
  uid: string,
  docId: string,
  subCollection: string,
  fieldName: string,
  index?: number
): Promise<string> {
  const docRef = getFirestore().doc(`${USERS_COLLECTION}/${uid}/${subCollection}/${docId}`);
  const docSnap = await docRef.get();
  if (!docSnap.exists) {
    throw new Error(`문서를 찾을 수 없습니다: ${docRef.path}`);
  }
  const data = docSnap.data();
  if (!data) return "{}";

  if (index === undefined || index === null) {
    const result = data[fieldName];
    if (typeof result === "undefined") {
      throw new Error(`"${fieldName}" 필드를 찾을 수 없습니다.`);
    }
    return typeof result === "string" ? result : JSON.stringify(result);
  } else {
    const result = data.roadmap[fieldName];
    if (typeof result === "undefined") {
      throw new Error(`"${fieldName}" 필드를 찾을 수 없습니다.`);
    }
    const stage = result[index];
    return typeof stage === "string" ? stage : JSON.stringify(stage);
  }
}
