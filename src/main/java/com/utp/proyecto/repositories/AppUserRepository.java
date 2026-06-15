package com.utp.proyecto.repositories;

import com.utp.proyecto.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    Optional<AppUser> findFirstByNameIgnoreCaseOrFullNameIgnoreCase(String name, String fullName);
}
