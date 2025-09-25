import {Type} from "@google/genai";

// 로드맵 응답 스키마 정의
export const roadmapResponseSchema = {
  type: Type.OBJECT,
  properties: {
    roadmap: {
      type: Type.OBJECT,
      properties: {
        title: {type: Type.STRING},
        description: {type: Type.STRING},
        stages: {
          type: Type.ARRAY,
          items: {
            type: Type.OBJECT,
            properties: {
              id: {type: Type.NUMBER},
              title: {type: Type.STRING},
              description: {type: Type.STRING},
              learning_curve: {type: Type.STRING},
              concept: {type: Type.STRING},
              status: {type: Type.BOOLEAN},
            },
            propertyOrdering: ["id", "title", "description", "learning_curve", "concept", "status"],
          },
        },
      },
      propertyOrdering: ["title", "description", "stages"],
    },
  },
  propertyOrdering: ["roadmap"],
};
