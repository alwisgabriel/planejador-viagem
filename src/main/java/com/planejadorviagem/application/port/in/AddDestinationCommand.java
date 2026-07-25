package com.planejadorviagem.application.port.in;

import java.util.UUID;

public record AddDestinationCommand(
        UUID tripId,
        String city,
        String country,
        int displayOrder
) {
}
