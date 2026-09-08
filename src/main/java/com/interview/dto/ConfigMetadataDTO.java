package com.interview.dto;

import com.interview.model.DifficultyLevel;
import com.interview.model.JobRole;

import java.util.ArrayList;
import java.util.List;

public class ConfigMetadataDTO {
    public static class RoleOption {
        private String code;
        private String displayName;
        private String description;

        public RoleOption(String code, String displayName, String description) {
            this.code = code;
            this.displayName = displayName;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class DifficultyOption {
        private String code;
        private String displayName;
        private String description;

        public DifficultyOption(String code, String displayName, String description) {
            this.code = code;
            this.displayName = displayName;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    private List<RoleOption> roles = new ArrayList<>();
    private List<DifficultyOption> difficulties = new ArrayList<>();
    private int defaultQuestionCount = 5;
    private int minQuestionCount = 1;
    private int maxQuestionCount = 10;

    public ConfigMetadataDTO() {
        for (JobRole role : JobRole.values()) {
            roles.add(new RoleOption(role.name(), role.getDisplayName(), role.getKeyDomains()));
        }
        for (DifficultyLevel diff : DifficultyLevel.values()) {
            difficulties.add(new DifficultyOption(diff.name(), diff.getDisplayName(), diff.getDescription()));
        }
    }

    public List<RoleOption> getRoles() {
        return roles;
    }

    public List<DifficultyOption> getDifficulties() {
        return difficulties;
    }

    public int getDefaultQuestionCount() {
        return defaultQuestionCount;
    }

    public int getMinQuestionCount() {
        return minQuestionCount;
    }

    public int getMaxQuestionCount() {
        return maxQuestionCount;
    }
}
