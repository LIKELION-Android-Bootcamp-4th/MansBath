import {
  getFirestore,
  DocumentReference,
} from "firebase-admin/firestore";
import {ConceptDetail, Quiz} from "../type/quiz_types";
import {HttpsError} from "firebase-functions/v2/https";

/**
 * 퀴즈 내용을 Firestore에 저장합니다.
 * @param {string} uid - 사용자 ID입니다.
 * @param {Quiz} quiz - 저장할 퀴즈 객체입니다.
 */
export async function saveQuiz(
  uid: string,
  quiz: Quiz
): Promise<Quiz> { // 반환 타입을 추가하여 저장된 데이터를 돌려줌
  const collectionRef = getFirestore()
    .collection("users")
    .doc(uid)
    .collection("quizzes")
    .doc(quiz.studyId)
    .collection("quiz");

  // 1. 문서 참조를 미리 만듭니다. 이 시점에 `quizRef.id`에 고유 ID가 생성됩니다.
  const quizRef = collectionRef.doc();

  // 2. quiz 객체에 고유 ID를 추가합니다.
  const quizDataWithId: Quiz = {
    ...quiz,
    quizId: quizRef.id,
  };

  // 3. ID가 포함된 최종 데이터를 Firestore에 저장합니다.
  await quizRef.set(quizDataWithId, {merge: true});

  // 4. ID가 포함된 완전한 객체를 반환합니다.
  return quizDataWithId;
}

/**
 * Firestore에 저장된 개념 상세 파일을 불러옵니다.
 * @param {string} uid - 사용자 ID입니다.
 * @param {string} studyId - 개념 상세 파일 ID입니다.
 */
export async function getConceptDetail(
  uid: string,
  studyId: string | undefined
) {
  const collectionRef = getFirestore().collection(`users/${uid}/studies`);
  if (typeof studyId === "string") {
    const conceptRef = collectionRef.doc(studyId);
    const doc = await conceptRef.get();
    return doc.data() as ConceptDetail;
  } else throw new HttpsError("invalid-argument", "studyId must be a string");
}
