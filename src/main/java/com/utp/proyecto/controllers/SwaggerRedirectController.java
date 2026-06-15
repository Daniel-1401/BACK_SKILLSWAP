package com.utp.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/api/v1/swagger-ui.html")
    public String redirectToSwaggerUi() {
        return "redirect:/api/v1/swagger-ui/index.html";
    }
}
