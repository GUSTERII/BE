package com.usv.examtimetabling.security.tokens;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, Integer> {
    Optional<Tokens> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<Tokens> findByToken(String token);
}
