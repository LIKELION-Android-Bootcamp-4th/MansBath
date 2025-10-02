package com.aspa2025.aspa2025.data.mapper

import com.aspa2025.aspa2025.data.dto.RoadmapDto
import com.aspa2025.aspa2025.model.Quiz
import com.aspa2025.aspa2025.model.Roadmap
import com.aspa2025.aspa2025.model.Section
import com.google.firebase.Timestamp

fun RoadmapDto.toRoadmap(roadmapId: String, questionId: String, createdAt: Timestamp?): Roadmap {
    val sections = this.stages.map { s ->
        Section(
            id = s.id,
            title = s.title,
            description = s.description,
            concept = s.concept,
            duration = s.learning_curve,
            status = s.status,
            isSolved = s.isSolved,
            quiz = Quiz()
        )
    }

    // status == true 인 section 개수 계산
    val completedSection = sections.count { it.status }

    return Roadmap(
        id = roadmapId,
        title = title,
        description = description,
        completedSection = completedSection,
        allSection = sections.size,
        sections = sections,
        questionId = questionId,
        createdAt = createdAt,
    )
}