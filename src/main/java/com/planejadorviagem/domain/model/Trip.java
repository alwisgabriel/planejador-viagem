package com.planejadorviagem.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class Trip {

    private final UUID id;
    private final UUID userId;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal budget;
    private final Status status;

    private Trip(
            UUID id,
            UUID userId,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal budget,
            Status status
    ) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.status = status;
    }

    public static Trip restore(UUID id, UUID userId, String title, LocalDate startDate, LocalDate endDate, BigDecimal budget, Status status) {
        return new Trip(id, userId, title, startDate, endDate, budget, status);
    }

    public static Trip planned(
            UUID userId,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal budget
    ) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "A data final deve ser igual ou posterior à data inicial"
            );
        }

        if (budget.signum() < 0) {
            throw new IllegalArgumentException("O orçamento não pode ser negativo");
        }

        return new Trip(
                UUID.randomUUID(),
                userId,
                title,
                startDate,
                endDate,
                budget,
                Status.PLANNED
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        PLANNED
    }
}
