package com.utp.proyecto.services;

import com.utp.proyecto.dto.ConversationResponse;
import com.utp.proyecto.dto.CreateConversationRequest;
import com.utp.proyecto.dto.CreateMessageRequest;
import com.utp.proyecto.dto.MessageResponse;
import com.utp.proyecto.exceptions.ApiException;
import com.utp.proyecto.models.AppUser;
import com.utp.proyecto.models.Conversation;
import com.utp.proyecto.models.Message;
import com.utp.proyecto.repositories.AppUserRepository;
import com.utp.proyecto.repositories.ConversationRepository;
import com.utp.proyecto.repositories.MessageRepository;
import com.utp.proyecto.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AppUserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ConversationService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            AppUserRepository userRepository,
            CurrentUserService currentUserService
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public ConversationResponse createOrGet(CreateConversationRequest request) {
        Long currentUserId = currentUserService.getCurrentUserId();
        if (currentUserId.equals(request.participantId())) {
            throw ApiException.badRequest("No puedes iniciar una conversación contigo mismo.");
        }
        return conversationRepository.findBetweenUsers(currentUserId, request.participantId())
                .map(c -> toConversationResponse(c, currentUserId))
                .orElseGet(() -> {
                    AppUser other = userRepository.findById(request.participantId())
                            .orElseThrow(() -> ApiException.notFound("Usuario no encontrado."));
                    Conversation conv = new Conversation();
                    conv.setParticipantOne(currentUserService.getCurrentUser());
                    conv.setParticipantTwo(other);
                    return toConversationResponse(conversationRepository.save(conv), currentUserId);
                });
    }

    public List<ConversationResponse> list() {
        Long currentUserId = currentUserService.getCurrentUserId();
        return conversationRepository.findByParticipantOneIdOrParticipantTwoIdOrderByUpdatedAtDesc(currentUserId, currentUserId).stream()
                .map(conversation -> toConversationResponse(conversation, currentUserId))
                .toList();
    }

    public List<MessageResponse> getMessages(Long conversationId) {
        Conversation conversation = findConversationForCurrentUser(conversationId);
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId()).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    public MessageResponse sendMessage(Long conversationId, CreateMessageRequest request) {
        if (request.body() == null || request.body().isBlank()) {
            throw ApiException.badRequest("body es requerido.");
        }

        Conversation conversation = findConversationForCurrentUser(conversationId);
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(currentUserService.getCurrentUser());
        message.setBody(request.body());
        Message saved = messageRepository.save(message);

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return toMessageResponse(saved);
    }

    private Conversation findConversationForCurrentUser(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Conversacion no encontrada."));
        if (!conversation.getParticipantOne().getId().equals(currentUserId)
                && !conversation.getParticipantTwo().getId().equals(currentUserId)) {
            throw ApiException.forbidden("No tienes acceso a esta conversacion.");
        }
        return conversation;
    }

    private ConversationResponse toConversationResponse(Conversation conversation, Long currentUserId) {
        AppUser participant = conversation.getParticipantOne().getId().equals(currentUserId)
                ? conversation.getParticipantTwo()
                : conversation.getParticipantOne();
        String lastMessage = messageRepository.findFirstByConversationIdOrderByCreatedAtDesc(conversation.getId())
                .map(Message::getBody)
                .orElse("");
        long unreadCount = messageRepository.countByConversationIdAndSenderIdNotAndReadByRecipientFalse(conversation.getId(), currentUserId);

        return new ConversationResponse(
                conversation.getId(),
                participant.getFullName(),
                participant.getAvatarUrl(),
                lastMessage,
                unreadCount,
                conversation.getUpdatedAt().toString()
        );
    }

    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getBody(),
                message.getCreatedAt().toString()
        );
    }
}
