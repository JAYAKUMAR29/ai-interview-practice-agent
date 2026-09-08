package com.interview.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobRole {
    JAVA_DEVELOPER("Java Developer", "Core Java, Spring Boot, OOP, Multithreading, JVM, Collections"),
    PYTHON_DEVELOPER("Python Developer", "Python syntax, Data Structures, OOP, Web Frameworks, Async, Decorators"),
    SOFTWARE_ENGINEER("Software Engineer", "System Design, Algorithms, Clean Code, Testing, CI/CD, DB Architecture"),
    DATA_ANALYST("Data Analyst", "SQL, Python/Pandas, Data Cleaning, Statistics, Visualization, Reporting"),
    WEB_DEVELOPER("Web Developer", "HTML/CSS, Modern JavaScript, REST APIs, Frontend Architecture, State, Responsive Design");

    private final String displayName;
    private final String keyDomains;

    JobRole(String displayName, String keyDomains) {
        this.displayName = displayName;
        this.keyDomains = keyDomains;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKeyDomains() {
        return keyDomains;
    }

    @JsonValue
    public String toJson() {
        return name();
    }

    @JsonCreator
    public static JobRole fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.trim().toUpperCase().replace(" ", "_");
        for (JobRole role : values()) {
            if (role.name().equalsIgnoreCase(normalized) || role.displayName.equalsIgnoreCase(value.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown Job Role: " + value);
    }
}
