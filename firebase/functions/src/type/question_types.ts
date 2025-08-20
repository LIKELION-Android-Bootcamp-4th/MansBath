import {FieldValue} from "firebase-admin/firestore";

// Gemini와의 대화 기록 타입
export interface HistoryEntry {
  role: "user" | "model";
  message: string | object;
  createdAt: string;
}

// Gemini의 응답 JSON 구조 타입
export interface AiResponse {
  message: string;
  choices?: string[];
  result?: { [key: string]: any };
  createdAt: string;
}

// Firestore에 저장될 질문 데이터 타입
export interface QuestionData {
  history: HistoryEntry[];
  lastUpdatedAt: FieldValue;
  lastMessage: string;
  createdAt?: FieldValue;
  title?: string;
  result?: { [key: string]: any };
}

// Gemini API에 전달할 선택지 전달 타입
export interface AskQuestionArgs {
  message: string;
  choices: string[];
}

// Gemini API에 최종 분석서를 생성할 때 전달할 타입
export interface CreateAnalysisArgs {
  message: string;
  result: string;
}
