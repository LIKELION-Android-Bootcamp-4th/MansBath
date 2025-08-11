import {GoogleGenerativeAI} from "@google/generative-ai";
import * as dotenv from "dotenv";
dotenv.config();

const GEMINI_API_KEY = process.env.GEMINI_API_KEY!;

const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);

export const model = genAI.getGenerativeModel({model: "gemini-2.5-flash"});
