package com.utp.proyecto.repositories;

import com.utp.proyecto.models.UserSkill;
import com.utp.proyecto.models.UserSkillType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUserId(Long userId);
    List<UserSkill> findByUserIdAndType(Long userId, UserSkillType type);
    List<UserSkill> findByType(UserSkillType type);
}
