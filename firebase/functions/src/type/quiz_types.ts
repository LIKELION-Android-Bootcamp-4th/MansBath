import {FieldValue} from "firebase-admin/firestore";

export interface QuizQuestion {
  question: string;
  options: string[];
  answer: string;
  description: string;
}

export interface Quiz {
  quizTitle: string;
  questions: QuizQuestion[];
  studyId: string;
  roadmapId: string;
  createdAt: FieldValue;
  status: boolean;
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

export interface RoadmapDto {
  questionId: string;
  roadmap: {
    description: string;
    stages: [];
    title: string;
  };
  createdAt: FieldValue;
}
