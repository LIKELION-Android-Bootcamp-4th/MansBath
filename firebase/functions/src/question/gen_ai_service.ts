import {getAiModel} from "../ai/gen_ai";
import {logger} from "firebase-functions";
import {QUESTION_SYSTEM_PROMPT} from "../ai/question_prompt";
import {HistoryEntry, AiResponse, CreateAnalysisArgs, AskQuestionArgs} from "../type/question_types";
import {FunctionCall, FunctionDeclarationsTool, SchemaType} from "@google/generative-ai";

/**
 * 응답 구조 규격화 정의.
 */
export const questionServiceTools: FunctionDeclarationsTool[] = [
  {
    functionDeclarations: [
      {
        name: "askQuestion",
        description: "사용자에게 학습 목표를 더 잘 이해하기 위한 객관식 질문을 합니다.",
        parameters: {
          type: SchemaType.OBJECT,
          properties: {
            message: {
              type: SchemaType.STRING,
              description: "사용자에게 전달할 질문 메시지. 반드시 한 번에 하나의 질문만 포함해야 합니다.",
            },
            choices: {
              type: SchemaType.ARRAY,
              description: "사용자가 선택할 수 있는 4개의 객관식 선택지.",
              items: {type: SchemaType.STRING},
            },
          },
          required: ["message", "choices"],
        },
      },
      {
        name: "createAnalysis",
        description: "사용자와의 대화에서 충분한 정보를 수집했을 때 최종 분석서를 생성합니다.",
        parameters: {
          type: SchemaType.OBJECT,
          properties: {
            message: {
              type: SchemaType.STRING,
              description: "분석서가 준비되었음을 알리는 간단한 메시지.",
            },
            result: {
              type: SchemaType.STRING,
              description: "대화 내용을 기반으로 동적으로 생성된 분석 결과를 담은 JSON 형식의 문자열입니다. 예: '{\"학습 동기\":\"애니 시청\",\"목표\":\"자유로운 회화\"}'",
            },
          },
          required: ["message", "result"],
        },
      },
    ],
  },
];

/**
 * Gemini에 전달할 대화 기록 배열을 생성합니다.
 * @param {HistoryEntry[]} history - 기존 대화 기록.
 * @return {object[]} Gemini API 형식에 맞는 대화 기록.
 */
function buildChatHistory(history: HistoryEntry[]) {
  const geminiHistory = history.map((entry) => {
    const content = typeof entry.message === "string" ?
      entry.message :
      JSON.stringify(entry.message);
    return {role: entry.role, parts: [{text: content}]};
  });

  // 시스템 프롬프트와 함께 전체 대화 기록 구성
  return [
    {role: "user" as const, parts: [{text: QUESTION_SYSTEM_PROMPT}]},
    {role: "model" as const, parts: [{text: "네, 알겠습니다."}]},
    ...geminiHistory,
  ];
}

/**
 * Gemini AI에게 응답을 요청하고 결과를 파싱합니다.
 * @param {HistoryEntry[]} history - 기존 대화 기록.
 * @param {string} userQuestion - 사용자의 새 질문.
 * @return {Promise<AiResponse>} 파싱된 AI 응답.
 */
export async function getAiResponse(
  history: HistoryEntry[],
  userQuestion: string,
): Promise<AiResponse> {
  try {
    const model = getAiModel();
    const fullHistoryForAI = buildChatHistory(history);

    // 대화 시작 시 이 서비스에 필요한 tools(응답 규격화 정보)를 전달합니다.
    const chat = model.startChat({
      history: fullHistoryForAI,
      tools: questionServiceTools,
    });

    const result = await chat.sendMessage(userQuestion);
    const response = result.response;
    const functionCalls = response.functionCalls();

    if (functionCalls && functionCalls.length > 0) {
      return handleFunctionCall(functionCalls[0]);
    } else {
      return {
        message: response.text(),
        createdAt: new Date().toISOString(),
      };
    }
  } catch (error) {
    logger.error("Error getting AI response:", {error});
    return {
      message: "죄송합니다, 응답을 생성하는 중에 오류가 발생했습니다.",
      createdAt: new Date().toISOString(),
    };
  }
}

/**
 * 모델의 FunctionCall 응답을 AiResponse 형식으로 변환합니다.
 * @param {FunctionCall} call - 모델이 반환한 함수 호출 객체.
 * @return {AiResponse} 변환된 AI 응답 객체.
 */
function handleFunctionCall(call: FunctionCall): AiResponse {
  const {name, args} = call;
  const now = new Date().toISOString();

  switch (name) {
  case "askQuestion": {
    const typedArgs = args as AskQuestionArgs;

    return {
      message: typedArgs.message,
      choices: typedArgs.choices,
      createdAt: now,
    };
  }

  case "createAnalysis": {
    const typedArgs = args as CreateAnalysisArgs;

    try {
      const resultObject = JSON.parse(typedArgs.result);

      return {
        message: typedArgs.message,
        result: resultObject,
        createdAt: now,
      };
    } catch (e) {
      logger.error("AI 응답에서 result JSON을 파싱하는 데 실패했습니다.", {
        error: e,
        jsonString: typedArgs.result,
      });
      return {
        message: "분석 결과를 처리하는 중 오류가 발생했습니다.",
        result: {error: "Invalid JSON format received from AI", data: typedArgs.result},
        createdAt: now,
      };
    }
  }

  default:
    logger.warn("Unknown function call received:", {name});
    throw new Error(`Unknown function call: ${name}`);
  }
}
