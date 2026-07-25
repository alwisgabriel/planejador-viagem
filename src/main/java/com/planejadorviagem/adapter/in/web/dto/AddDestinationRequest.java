package com.planejadorviagem.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AddDestinationRequest(
        @NotBlank String city,
        @NotBlank String country,
        @Positive int displayOrder
) {}
