package com.utp.proyecto.controllers;

import com.utp.proyecto.dto.ConversationResponse;
import com.utp.proyecto.dto.CreateMessageRequest;
import com.utp.proyecto.dto.MessageResponse;
import com.utp.proyecto.services.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {
    private final ConversationService service;

    public ConversationController(ConversationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConversationResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}/messages")
    public List<MessageResponse> getMessages(@PathVariable Long id) {
        return service.getMessages(id);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable Long id, @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.sendMessage(id, request));
    }
}
