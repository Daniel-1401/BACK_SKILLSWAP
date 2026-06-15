package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByParticipantOneIdOrParticipantTwoIdOrderByUpdatedAtDesc(Long participantOneId, Long participantTwoId);
}
