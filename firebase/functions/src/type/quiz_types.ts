import {FieldValue} from "firebase-admin/firestore";

export interface QuizQuestion {
  question: string;
  options: string[];
  answer: string;
  description: string;
}

export interface Quiz {
  quizId: string;
  quizTitle: string;
  questions: QuizQuestion[];
  studyId: string;
  createdAt: FieldValue;
  status: boolean;
}

export interface CreateQuizArgs {
  quizTitle: string;
  questions: QuizQuestion[];
}

export interface ConceptDetail {
  title: string;
  description: string;
  duration: string;
  status: boolean;
  roadmapId: string;
  items: [{
    content: [{
      details: string;
      keypoints: string[];
      overview: string;
    }]
    subtitle: string[];
    title: string;
  }];
}
