package com.planejadorviagem.application.port.in;

import java.util.UUID;

public interface DeleteTripUseCase {

    void deleteById(UUID id);
}
