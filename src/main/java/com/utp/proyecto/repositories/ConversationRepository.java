package com.utp.proyecto.repositories;

import com.utp.proyecto.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByParticipantOneIdOrParticipantTwoIdOrderByUpdatedAtDesc(Long participantOneId, Long participantTwoId);

    @Query("SELECT c FROM Conversation c WHERE (c.participantOne.id = :a AND c.participantTwo.id = :b) OR (c.participantOne.id = :b AND c.participantTwo.id = :a)")
    Optional<Conversation> findBetweenUsers(@Param("a") Long a, @Param("b") Long b);
}
