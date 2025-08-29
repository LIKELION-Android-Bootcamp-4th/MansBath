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
  result?: string;
}

// Firestore에 저장될 질문 데이터 타입
export interface QuestionData {
  history: HistoryEntry[];
  lastUpdatedAt: FieldValue;
  lastMessage: string;
  createdAt?: FieldValue;
  title?: string;
  result?: string;
}
