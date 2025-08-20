import {logger} from "firebase-functions";
import {getAiModel} from "../ai/gen_ai";
import {SYSTEM_PROMPT} from "../ai/quiz_prompt";
import {ConceptDetail, Quiz, CreateQuizArgs} from "../type/quiz_types";
import {FieldValue} from "firebase-admin/firestore";
import {HttpsError} from "firebase-functions/v2/https";
import {FunctionCall, FunctionDeclarationsTool, SchemaType} from "@google/generative-ai";

export const quizServiceTools: FunctionDeclarationsTool[] = [
  {
    functionDeclarations: [
      {
        name: "createQuiz",
        description: "제공된 개념 상세 파일을 기반으로 객관식 퀴즈 10개를 생성합니다.",
        parameters: {
          type: SchemaType.OBJECT,
          properties: {
            quizTitle: {
              type: SchemaType.STRING,
              description: "생성된 퀴즈의 전체 주제나 제목입니다.",
            },
            questions: {
              type: SchemaType.ARRAY,
              description: "생성된 10개의 객관식 문제 배열입니다.",
              items: {
                type: SchemaType.OBJECT,
                properties: {
                  question: {
                    type: SchemaType.STRING,
                    description: "퀴즈 질문 내용입니다.",
                  },
                  options: {
                    type: SchemaType.ARRAY,
                    description: "4개의 선택지 배열입니다.",
                    items: {type: SchemaType.STRING},
                  },
                  answer: {
                    type: SchemaType.STRING,
                    description: "4개의 선택지 중 정답에 해당하는 문자열입니다.",
                  },
                  description: {
                    type: SchemaType.STRING,
                    description: "정답에 대한 간단한 해설입니다.",
                  },
                },
                required: ["question", "options", "answer", "description"],
              },
            },
          },
          required: ["quizTitle", "questions"],
        },
      },
    ],
  },
];

/**
 * Gemini AI에게 퀴즈 생성을 요청하고 결과를 파싱합니다.
 * @param {ConceptDetail} conceptDetail - 퀴즈 생성 시 필요한 개념 상세 파일입니다.
 * @param {string} studyId - studyId
 * @return {Promise<Quiz>} 파싱된 AI 응답입니다.
 */
export async function getQuiz(
  conceptDetail: ConceptDetail,
  studyId: string,
): Promise<Quiz> {
  const model = getAiModel();
  const chat = model.startChat({
    tools: quizServiceTools,
  });

  const prompt = SYSTEM_PROMPT + JSON.stringify(conceptDetail);

  try {
    const result = await chat.sendMessage(prompt);
    const response = result.response;
    const functionCalls = response.functionCalls();

    if (functionCalls && functionCalls.length > 0) {
      return handleQuizFunctionCall(functionCalls[0], studyId);
    } else {
      logger.error("AI did not return a function call.", {responseText: response.text()});
      throw new HttpsError("internal", "AI did not generate a valid quiz structure.");
    }
  } catch (error) {
    logger.error("Error getting AI response for quiz:", {error});
    if (error instanceof HttpsError) throw error;
    throw new HttpsError("internal", "Failed to get response from AI for quiz.");
  }
}

/**
 * 'createQuiz' FunctionCall 응답을 Quiz 객체 형식으로 변환합니다.
 * @param {FunctionCall} call - 모델이 반환한 함수 호출 객체
 * @param {string} studyId - 현재 학습 ID
 * @return {Quiz} 변환된 퀴즈 객체
 */
function handleQuizFunctionCall(call: FunctionCall, studyId: string): Quiz {
  if (call.name !== "createQuiz") {
    throw new HttpsError("internal", `Unexpected function call from AI: ${call.name}`);
  }

  const args = call.args as CreateQuizArgs;

  return {
    quizId: "",
    studyId: studyId,
    quizTitle: args.quizTitle,
    questions: args.questions,
    createdAt: FieldValue.serverTimestamp(),
    status: false,
  };
}
