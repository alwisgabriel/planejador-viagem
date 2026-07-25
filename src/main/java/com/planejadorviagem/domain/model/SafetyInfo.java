package com.planejadorviagem.domain.model;

public record SafetyInfo(
        String city,
        SafetyLevel level,
        String description
) {

    public enum SafetyLevel {
        LOW, MODERATE, HIGH
    }
}
