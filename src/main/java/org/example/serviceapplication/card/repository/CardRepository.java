package org.example.serviceapplication.card.repository;

import org.example.serviceapplication.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByUserId(Long userId);
    Optional<Card> findById(Long id);
}
