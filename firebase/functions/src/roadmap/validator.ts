import {RoadmapBody} from "../type/roadmap_types";

/**
 * 요청 body에서 `uid`와 `questionId`를 검증하고 반환합니다.
 *
 * @param {RoadmapBody} body - 클라이언트로부터 전달받은 요청 body
 * @return {{uid: string, questionId: string}} 검증이 완료된 uid와 questionId 객체
 * @throws {Error} uid 또는 questionId가 누락되었거나 문자열이 아닌 경우
 */
export function validateBody(
  body: RoadmapBody
): {uid: string; questionId: string} {
  const uid = body?.uid;
  const questionId = body?.questionId;

  if (!uid || typeof uid !== "string") {
    console.error("❌ validateBody 실패: uid가 없거나 문자열이 아님", {uid});
    throw new Error("uid는 필수 문자열입니다.");
  }
  if (!questionId || typeof questionId !== "string") {
    console.error(
      "❌ validateBody 실패: questionId가 없거나 문자열이 아님",
      {questionId}
    );
    throw new Error("questionId는 필수 문자열입니다.");
  }
  return {uid, questionId};
}
