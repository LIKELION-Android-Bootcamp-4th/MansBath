import {GoogleGenerativeAI, GenerativeModel} from "@google/generative-ai";
import * as dotenv from "dotenv";

dotenv.config();

let model: GenerativeModel;

/**
 * Gemini AI 모델 인스턴스를 가져오는 함수
 * @return {GenerativeModel} 초기화된 AI 모델
 */
export function getAiModel(): GenerativeModel {
  // 모델이 아직 초기화되지 않았을 경우에만 아래 로직을 실행
  if (!model) {
    // 1. API 키 확인
    const GEMINI_API_KEY = process.env.GEMINI_API_KEY;
    if (!GEMINI_API_KEY) {
      throw new Error("GEMINI_API_KEY 환경 변수가 설정되지 않았습니다.");
    }

    // 2. AI 클라이언트 및 모델 초기화
    const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);
    model = genAI.getGenerativeModel({model: "gemini-2.5-flash"});
  }

  // 이미 생성된 모델 인스턴스를 반환합니다.
  return model;
}
