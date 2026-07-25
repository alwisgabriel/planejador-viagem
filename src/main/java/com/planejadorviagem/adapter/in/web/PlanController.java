package com.planejadorviagem.adapter.in.web;

import com.planejadorviagem.adapter.in.web.dto.PlanResponse;
import com.planejadorviagem.application.port.in.GeneratePlanUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class PlanController {

    private final GeneratePlanUseCase generatePlanUseCase;

    public PlanController(GeneratePlanUseCase generatePlanUseCase) {
        this.generatePlanUseCase = generatePlanUseCase;
    }

    @PostMapping("/{tripId}/plan")
    public ResponseEntity<PlanResponse> generatePlan(@PathVariable UUID tripId) {
        var plan = generatePlanUseCase.generate(tripId);
        return ResponseEntity.ok(PlanResponse.from(plan));
    }
}
