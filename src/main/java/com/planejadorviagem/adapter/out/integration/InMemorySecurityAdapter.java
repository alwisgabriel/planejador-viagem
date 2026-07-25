package com.planejadorviagem.adapter.out.integration;

import com.planejadorviagem.application.port.out.SecurityPort;
import com.planejadorviagem.domain.model.SafetyInfo;

import java.util.Map;

public final class InMemorySecurityAdapter implements SecurityPort {

    private static final Map<String, SafetyInfo> DATA = Map.of(
            "São Paulo", new SafetyInfo("São Paulo", SafetyInfo.SafetyLevel.MODERATE, "Áreas turísticas são seguras durante o dia"),
            "Rio de Janeiro", new SafetyInfo("Rio de Janeiro", SafetyInfo.SafetyLevel.MODERATE, "Evite áreas isoladas à noite"),
            "Brasil", new SafetyInfo("Brasil", SafetyInfo.SafetyLevel.MODERATE, "Cuidados padrão com grandes centros urbanos")
    );

    @Override
    public SafetyInfo getSafety(String city, String country) {
        return DATA.getOrDefault(city,
                new SafetyInfo(city, SafetyInfo.SafetyLevel.MODERATE, "Informações de segurança não disponíveis para esta localidade"));
    }
}
