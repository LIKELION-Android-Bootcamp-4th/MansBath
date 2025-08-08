import {
  getFirestore,
  DocumentReference,
} from "firebase-admin/firestore";
import {ConceptDetail, Quiz} from "./types";

/**
 * 퀴즈 내용을 Firestore에 저장합니다.
 * @param {string} uid - 사용자 ID입니다.
 * @param {Quiz} quiz - 저장할 퀴즈 객체입니다.
 */
export async function saveQuiz(
  uid: string,
  quiz: Quiz
) {
  const collectionRef = getFirestore().collection(`users/${uid}/quizzes`);
  const quizRef: DocumentReference = collectionRef.doc();
  await quizRef.set(quiz, {merge: true});
}

/**
 * Firestore에 저장된 개념 상세 파일을 불러옵니다.
 * @param {string} uid - 사용자 ID입니다.
 * @param {string} quizName - 퀴즈의 이름입니다.
 */
export async function getConceptDetail(
  uid: string,
  quizName: string | undefined
) {
  const collectionRef = getFirestore().collection(`users/${uid}/studies`);
  if (typeof quizName === "string") {
    const conceptRef = collectionRef.doc(quizName);
    const doc = await conceptRef.get();
    return doc.data() as ConceptDetail;
  } else return "no Concept file found";
}
