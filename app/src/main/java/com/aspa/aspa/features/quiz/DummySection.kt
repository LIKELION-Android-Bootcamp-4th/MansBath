package com.aspa.aspa.features.quiz

import com.aspa.aspa.model.Quiz
import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.model.Section

class DummySection {

    companion object {
        val dummyRoadmapList = listOf(
            Roadmap(
                title = "React 완전 정복",
                description = "React 기초부터!",
                completedSection = 1,
                allSection = 2,
                sections = listOf(
                    Section(
                        title = "JavaScript 기초",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = true,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),
                    Section(
                        title = "JavaScript 핵심",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = false,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),

                    )
            ),

            Roadmap(
                title = "React 완전 정복",
                description = "React 기초부터!",
                completedSection = 0,
                allSection = 2,
                sections = listOf(
                    Section(
                        title = "JavaScript 기초",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = false,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),
                    Section(
                        title = "JavaScript 핵심",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = false,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),
                )
            ),
            Roadmap(
                title = "React 완전 정복",
                description = "React 기초부터!",
                completedSection = 2,
                allSection = 2,
                sections = listOf(
                    Section(
                        title = "JavaScript 기초",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = true,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),
                    Section(
                        title = "JavaScript 핵심",
                        description = "description",
                        concept = "concept",
                        duration = "4~6주",
                        status = true,
                        quiz = Quiz(
                            question = "TODO()",
                            choices = listOf("TODO1()", "answer", "TODO2()", "TODO3()"),
                            answer = "answer",
                            chosen = "answer",
                            description = ""
                        )
                    ),
                )
            )
        )
    }



}