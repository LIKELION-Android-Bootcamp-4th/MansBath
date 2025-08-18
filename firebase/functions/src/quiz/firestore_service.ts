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
) {
  const collectionRef = getFirestore()
    .collection("users")
    .doc(uid)
    .collection("quizzes")
    .doc(quiz.roadmapId)
    .collection("quiz");
  const quizRef: DocumentReference = collectionRef.doc();
  await quizRef.set(quiz, {merge: true});
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
