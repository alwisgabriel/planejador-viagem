package com.planejadorviagem.adapter.in.web.dto;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        String token
) {}
