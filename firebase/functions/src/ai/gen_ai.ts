import {GoogleGenerativeAI} from "@google/generative-ai";
import * as dotenv from "dotenv";

// .env 파일 읽기
dotenv.config();

// Gemini 설정
const GEMINI_API_KEY = process.env.GEMINI_API_KEY;
if (!GEMINI_API_KEY) {
  throw new Error("GEMINI_API_KEY is not defined");
}

const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);

// 모델과 프롬프트를 export하여 다른 파일에서 사용
export const model = genAI.getGenerativeModel({model: "gemini-2.5-flash"});
