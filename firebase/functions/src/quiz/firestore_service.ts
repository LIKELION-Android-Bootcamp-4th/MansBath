import {
  getFirestore,
  DocumentReference,
  FieldValue,
} from "firebase-admin/firestore";
import {ConceptDetail, Quiz, RoadmapDto} from "../type/quiz_types";
import {HttpsError} from "firebase-functions/v2/https";

/**
 * 퀴즈 내용을 Firestore에 저장합니다.
 * @param {string} uid - 사용자 ID입니다.
 * @param {Quiz} quiz - 저장할 퀴즈 객체입니다.
 * @param {string} roadmapId - 저장 경로에 필요한 roadmapID 입니다.
 */
export async function saveQuiz(
  uid: string,
  quiz: Quiz,
  roadmapId: string
) {
  const roadmapRef = getFirestore()
    .collection(`users/${uid}/roadmap`)
    .doc(roadmapId);
  const roadmapDto = (await roadmapRef.get()).data() as RoadmapDto;

  const collectionRef = getFirestore()
    .collection("users")
    .doc(uid)
    .collection("quizzes")
    .doc(roadmapId);
  const quizRef: DocumentReference = collectionRef.collection("quiz").doc();
  const batch = getFirestore().batch();

  if (roadmapDto == null) {
    throw new HttpsError("invalid-argument", "roadmap data is not found");
  } else {
    batch.set(
      collectionRef,
      {
        lastModified: FieldValue.serverTimestamp(),
        title: roadmapDto.roadmap.title,
        description: roadmapDto.roadmap.description,
      },
      {merge: true}
    );

    batch.set(quizRef, quiz);

    await batch.commit();
  }

  // await quizRef.set(quiz, {merge: true});
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
