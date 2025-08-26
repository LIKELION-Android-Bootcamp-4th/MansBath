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
 * 로드맵을 Firestore에 저장하고 해당 question 문서에 roadmapId를 연결합니다.
 * 이미 question 문서에 roadmapId가 존재하면 새로운 문서를 생성하지 않고 기존 roadmapId를 반환합니다.
 *
 * @param {string} uid - Firestore users 컬렉션의 사용자 UID
 * @param {string} questionId - 연결할 question 문서의 ID
 * @param {Roadmap} roadmap - 저장할 로드맵 데이터
 * @return {Promise<string>} - 생성되었거나 기존에 존재하는 로드맵 문서 ID
 * @throws {Error} - 문서 조회나 저장 과정에서 오류 발생 시
 *
 * @example
 * const roadmapId = await saveRoadmap("user123", "q456", { title: "AI 학습 플랜", stages: [...] });
 * console.log(roadmapId); // 새로 생성된 ID 또는 기존 roadmapId
 */
export async function saveRoadmap(
  uid: string, questionId: string, roadmap: Roadmap
): Promise<string> {
  try {
    const roadmapId = await getRoadmapIdFromQuestion(uid, questionId);

    if (roadmapId != null) {
      return roadmapId;
    }

    const ref = await getFirestore()
      .collection(`users/${uid}/roadmap`)
      .add({
        ...roadmap,
        questionId,
        createdAt: new Date(),
      });

    await addRoadmapIdToQuestion(uid, questionId, ref.id);

    return ref.id;
  } catch (err) {
    console.error(`❌ Firestore 저장 실패 (uid: ${uid})`, err);
    throw new Error("로드맵 저장 중 서버 오류가 발생했습니다.");
  }
}

/**
 * Question 문서에서 roadmapId 필드를 조회합니다.
 *
 * @param {string} uid - Firestore 상위 users 컬렉션의 사용자 UID
 * @param {string} questionId - 조회할 Question 문서의 ID
 * @return {Promise<string | null>} - roadmapId가 존재하면 string, 없으면 null
 */
export async function getRoadmapIdFromQuestion(
  uid: string,
  questionId: string
): Promise<string | null> {
  try {
    const docRef = await getFirestore()
      .collection(`users/${uid}/question`)
      .doc(questionId)
      .get();

    if (!docRef.exists) {
      throw new Error(`❌ Question 문서(${questionId})가 존재하지 않습니다.`);
    }

    const data = docRef.data();
    return data?.roadmapId ?? null; // 있으면 string, 없으면 null
  } catch (err) {
    console.error(
      `❌ Question 조회 실패 (uid: ${uid}, questionId: ${questionId})`,
      err
    );
    throw new Error("Question 조회 중 서버 오류가 발생했습니다.");
  }
}

/**
 * 특정 사용자의 Question 문서에 roadmapId 필드를 추가하거나 업데이트합니다.
 *
 * @param {string} uid - Firestore 상위 users 컬렉션의 사용자 UID
 * @param {string} questionId - 업데이트할 Question 문서의 ID
 * @param {string} roadmapId - Question 문서에 저장할 Roadmap 문서 ID
 * @return {Promise<void>} - 업데이트 완료 시 resolve, 실패 시 에러 throw
 *
 * @example
 * await addRoadmapIdToQuestion("user123", "question456", "roadmap789");
 * // 결과: users/user123/question/question456 문서에 { roadmapId: "roadmap789" } 추가
 */
async function addRoadmapIdToQuestion(
  uid:string, questionId: string, roadmapId: string
) {
  try {
    await getFirestore()
      .collection(`users/${uid}/question`)
      .doc(questionId)
      .set({roadmapId: roadmapId}, {merge: true});

    console.log(`✅ Question(${questionId})에 roadmapId(${roadmapId}) 추가 완료`);
  } catch (err) {
    console.error(`❌ Question의 roadmapId 부여 중 실패 (uid: ${uid}, questionId: ${questionId})`, err);
    throw new Error("Question의 roadmapId 부여 중 서버 오류가 발생했습니다.");
  }
}
