package com.aspa.aspa.core.constants.enums

enum class RedirectType {
    NONE, ROADMAP, ROADMAP_STATUS;

    companion object {
        fun from(value: String?): RedirectType =
            when (value) {
                "roadmap" -> ROADMAP
                "roadmap_status" -> ROADMAP_STATUS
                else -> NONE
            }
    }
}