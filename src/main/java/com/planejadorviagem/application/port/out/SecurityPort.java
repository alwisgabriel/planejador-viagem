package com.planejadorviagem.application.port.out;

import com.planejadorviagem.domain.model.SafetyInfo;

public interface SecurityPort {

    SafetyInfo getSafety(String city, String country);
}
