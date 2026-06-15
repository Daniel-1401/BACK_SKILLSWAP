package com.utp.proyecto.dto;

import java.math.BigDecimal;
import java.util.List;

public record PlanResponse(String id, String name, BigDecimal price, String billingPeriod, Integer credits, List<String> features) {
}
