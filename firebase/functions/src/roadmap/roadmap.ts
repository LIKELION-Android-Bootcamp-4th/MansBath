import {getFirestore} from "firebase-admin/firestore";
import {Request, Response} from "express";
import {model} from "../ai/gen_ai";
import {ROADMAP_SYSTEM_PROMPT} from "../ai/roadmap_prompt";
import {EXTRA_INSTRUCTION} from "../ai/common_prompt";
import {Roadmap} from "../type/roadmap_types";
import {formatResultFromFirestore} from "../util/formatter";
import {cleanAndParseGeminiResponse} from "../util/parser";


// Cloud Function 정의
export const generateRoadmap = async (req: Request, res: Response) => {
  try {
    const {uid, questionId} = req.body;

    if (!uid || !questionId) {
      res.status(400).send("uid와 questionId는 필수입니다.");
      return;
    }

    const docRef = getFirestore().doc(`users/${uid}/questions/${questionId}`);
    const docSnap = await docRef.get();

    if (!docSnap.exists) {
      res.status(404).send("해당 질문 문서를 찾을 수 없습니다.");
      return;
    }

    const result = docSnap.data()?.result;
    if (!result) {
      res.status(400).send("result 필드가 존재하지 않습니다.");
      return;
    }

    const resultString = formatResultFromFirestore(result);

    const fullPrompt = `
    ${ROADMAP_SYSTEM_PROMPT}
    [ 사용자 질문 분석서 ] 는 아래와 같아.
    ${resultString}
    ${EXTRA_INSTRUCTION}
    `;

    const response = await model.generateContent({
      contents: [{role: "user", parts: [{text: fullPrompt}]}],
    });

    const aiReply = response.response.text();

    // JSON 파싱 시도
    const parsedRoadmap = cleanAndParseGeminiResponse<Roadmap>(aiReply);

    // Firestore에 저장
    const roadmapRef = await getFirestore()
      .collection(`users/${uid}/roadmap`)
      .add(parsedRoadmap);

    res.status(200).json({
      message: "로드맵이 저장되었습니다.",
      docId: roadmapRef.id,
      data: parsedRoadmap,
    });
  } catch (err) {
    console.error("오류 발생:", err);
    res.status(500).send("서버 오류가 발생했습니다.");
  }
};
