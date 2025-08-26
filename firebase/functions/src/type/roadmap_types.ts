export interface Roadmap {
  title: string;
  questionId: string;
  description: string;
  stages: Stage[];
}

export interface Stage {
  id: number;
  title: string;
  description: string;
  learning_curve: string;
  concept: string;
  status: boolean;
}

export interface RoadmapBody {
    uid: string;
  questionId: string;
}
