import {
  getFirestore,
  FieldValue,
  DocumentReference,
} from "firebase-admin/firestore";
import {HistoryEntry, AiResponse, QuestionData} from "../type/question_types";

/**
 * Firestore에서 질문 기록을 가져오거나 새로 생성합니다.
 * @param {string} uid - 사용자 ID.
 * @param {string | undefined} questionId - 질문 ID.
 * @return {Promise<object>} questionRef, history, 확정된 questionId를 반환.
 */
export async function getOrCreateQuestion(
  uid: string,
  questionId: string | undefined,
) {
  const collectionRef = getFirestore().collection(`users/${uid}/questions`);
  let questionRef: DocumentReference;
  let history: HistoryEntry[] = [];
  let newQuestionId = questionId;

  if (newQuestionId) {
    questionRef = collectionRef.doc(newQuestionId);
    const doc = await questionRef.get();
    if (doc.exists) {
      history = (doc.data()?.history || []) as HistoryEntry[];
    }
  } else {
    questionRef = collectionRef.doc();
    newQuestionId = questionRef.id;
  }
  return {questionRef, history, questionId: newQuestionId};
}

/**
 * 대화 내용을 Firestore에 저장합니다.
 * @param {DocumentReference} ref - 저장할 문서의 참조.
 * @param {object} data - 저장할 데이터 객체.
 */
export async function saveConversation(
  ref: DocumentReference,
  data: {
      history: HistoryEntry[],
      userQuestion: string,
      aiResponse: AiResponse,
    },
) {
  const {history, userQuestion, aiResponse} = data;
  const updatedHistory: HistoryEntry[] = [
    ...history,
    {role: "user", message: userQuestion, createdAt: new Date().toISOString()},
    {role: "model", message: aiResponse, createdAt: new Date().toISOString()},
  ];

  const dataToSave: Partial<QuestionData> = {
    history: updatedHistory,
    lastUpdatedAt: FieldValue.serverTimestamp(),
    lastMessage: aiResponse.message || "",
  };

  if (history.length === 0) {
    dataToSave.createdAt = FieldValue.serverTimestamp();
    dataToSave.title = userQuestion.substring(0, 20);
  }
  if (aiResponse.result) {
    dataToSave.result = aiResponse.result;
  }

  await ref.set(dataToSave, {merge: true});
}
