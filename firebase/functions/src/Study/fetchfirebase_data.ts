import {getFirestore} from "firebase-admin/firestore";

/**
 * Firestore에서 지정된 사용자 문서의 result 필드를 가져옵니다.
 *
 * @param {string} uid - 사용자 UID
 * @param {string} docId - 문서 ID
 * @param {string} subCollection - 하위 컬렉션 이름
 * @param {string} fieldName - 필드 이름
 * @return {Promise<string>} result 필드 값
 * @throws {Error} 문서가 존재하지 않거나 result 필드가 없을 경우
 */
export async function fetchFirebaseData(
  uid: string,
  docId: string,
  subCollection: string,
  fieldName? : string,
): Promise<string> {
  const docRef = getFirestore().doc(`users/${uid}/${subCollection}/${docId}`);
  const docSnap = await docRef.get();

  if (!docSnap.exists) {
    throw new Error(`문서를 찾을 수 없습니다: users/${uid}/${subCollection}/${docId}`);
  }

  const data = docSnap.data();

  if (fieldName) {
    const result = data?.[fieldName];

    if (typeof result === "undefined") {
      throw new Error(`"${fieldName}" 필드를 찾을 수 없습니다.`);
    }

    // 문자열이 아니면 JSON 문자열로 변환
    if (typeof result === "string") {
      return result;
    } else {
      return JSON.stringify(result);
    }
  }

  // fieldName이 없으면 문서 전체를 문자열로 반환
  return JSON.stringify(data);
}
