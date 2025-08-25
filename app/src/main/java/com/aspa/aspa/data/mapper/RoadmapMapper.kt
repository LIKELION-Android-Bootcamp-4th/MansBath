package com.aspa.aspa.data.mapper

import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.model.Quiz
import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.model.Section
import com.google.firebase.Timestamp

fun RoadmapDto.toRoadmap(roadmapId: String, questionId: String, createdAt: Timestamp?): Roadmap {
    val sections = this.stages.map { s ->
        Section(
            id = s.id,
            title = s.title,
            description = s.description,
            concept = s.concept,
            duration = s.learning_curve,
            status = false,
            quiz = Quiz()
        )
    }

    return Roadmap(
        id = roadmapId,
        title = title,
        description = description,
        completedSection = 0,
        allSection = sections.size,
        sections = sections,
        questionId = questionId,
        createdAt = createdAt,
    )
}