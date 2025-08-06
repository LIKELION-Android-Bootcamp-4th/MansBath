export interface QuizQuestion {
  question: string;
  options: string[];
  answer: string;
  explanation: string;
}

export interface Quiz {
  quizTitle: string;
  questions: QuizQuestion[];
}

export interface ConceptDetail {
  step: number;
  title: string;
  learningObjective: string;
  mainContent: {
    keyPoints: string[];
    description: string;
    details: Array<{
      title: string;
      items: string[];
    }>;
  };
}
