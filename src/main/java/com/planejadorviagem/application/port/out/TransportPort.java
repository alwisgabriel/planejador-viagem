package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.TransportRecommendation;

public interface TransportPort {

    TransportRecommendation recommend(String origin, String destination);
}
