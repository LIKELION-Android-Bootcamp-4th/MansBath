import {Type} from "@google/genai";

export const quizResponseSchema = {
  type: Type.OBJECT,
  properties: {
    quizTitle: {type: Type.STRING},
    questions: {
      type: Type.ARRAY,
      items: {
        type: Type.OBJECT,
        properties: {
          question: {type: Type.STRING},
          options: {
            type: Type.ARRAY,
            items: {type: Type.STRING},
          },
          answer: {type: Type.STRING},
          description: {type: Type.STRING},
        },
        propertyOrdering: ["question", "options", "answer", "description"],
      },
    },
  },
  propertyOrdering: ["quizTitle", "questions"],
};
