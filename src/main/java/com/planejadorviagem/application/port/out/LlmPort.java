package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.GeneratedPlan;

public interface LlmPort {

    GeneratedPlan generate(String prompt);
}
