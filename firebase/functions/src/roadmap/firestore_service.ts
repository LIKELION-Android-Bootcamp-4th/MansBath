import {getFirestore} from "firebase-admin/firestore";
import {Roadmap} from "../type/roadmap_types";

/**
 * Firestore에서 특정 사용자의 질문 문서를 조회하고,
 * 존재 여부 및 `result` 필드를 검증한 뒤 반환합니다.
 *
 * @param {string} uid - 사용자 UID
 * @param {string} questionId - 질문 문서 ID
 * @return {Promise<Record<string, string>>} Firestore에서 가져온 result 객체
 * @throws {Error} 문서가 존재하지 않거나 result 필드가 없을 경우
 */
export async function getQuestionResult(
  uid: string,
  questionId: string
): Promise<Record<string, string>> {
  try {
    const docRef = getFirestore().doc(`users/${uid}/questions/${questionId}`);
    const docSnap = await docRef.get();

    if (!docSnap.exists) {
      console.error(
        "❌ getQuestionResult 실패: 문서 없음",
        {uid, questionId}
      );
      throw new Error("해당 질문 문서를 찾을 수 없습니다.");
    }

    const result = docSnap.data()?.result;
    if (!result) {
      console.error(
        "❌ getQuestionResult 실패: result 필드 없음",
        {uid, questionId, data: docSnap.data()}
      );
      throw new Error("result 필드가 존재하지 않습니다.");
    }

    return result as Record<string, string>;
  } catch (err) {
    console.error("❌ Firestore 조회 중 예기치 못한 오류", {uid, questionId, err});
    throw err;
  }
}


/**
 * 로드맵을 Firestore에 저장하고 문서 ID를 반환합니다.
 *
 * @param {string} uid - 사용자 UID
 * @param {string} questionId - question ID
 * @param {Roadmap} roadmap - 저장할 로드맵 데이터
 * @return {Promise<string>} 생성된 문서 ID
 * @throws {Error} 저장 실패 시 에러
 */
export async function saveRoadmap(
  uid: string, questionId: string, roadmap: Roadmap
): Promise<string> {
  try {
    const ref = await getFirestore()
      .collection(`users/${uid}/roadmap`)
      .add({
        ...roadmap,
        questionId,
        createdAt: new Date(),
      });

    return ref.id;
  } catch (err) {
    console.error(`❌ Firestore 저장 실패 (uid: ${uid})`, err);
    throw new Error("로드맵 저장 중 서버 오류가 발생했습니다.");
  }
}
