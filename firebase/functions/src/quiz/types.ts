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
  title: string;
  description: string;
  duration: string;
  status: boolean;
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
