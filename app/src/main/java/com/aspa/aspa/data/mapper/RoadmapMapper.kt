package com.aspa.aspa.data.mapper

import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.model.Quiz
import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.model.Section

fun RoadmapDto.toRoadmap(): Roadmap {
    val sections = stages.map { s ->
        Section(
            title = s.title,
            description = s.description,
            concept = s.concept,
            duration = s.learning_curve,
            status = false,
            quiz = Quiz(
                question = "TODO()",
                choices =  listOf("TODO()"),
                answer = "TODO()",
                description = "TODO()"
            )
        )
    }

    return Roadmap(
        title = title,
        description = description,
        completedSection = 0,
        allSection = sections.size,
        sections = sections
    )
}