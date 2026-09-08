package com.interview.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DifficultyLevel {
    BEGINNER("Beginner", "Foundational concepts, basic syntax, and definitions"),
    INTERMEDIATE("Intermediate", "Practical application, architectural choices, debugging, and framework internals"),
    ADVANCED("Advanced", "High-scale trade-offs, performance optimization, edge cases, and deep internals");

    private final String displayName;
    private final String description;

    DifficultyLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public String toJson() {
        return name();
    }

    @JsonCreator
    public static DifficultyLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        for (DifficultyLevel level : values()) {
            if (level.name().equalsIgnoreCase(normalized) || level.displayName.equalsIgnoreCase(value.trim())) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown Difficulty Level: " + value);
    }
}
