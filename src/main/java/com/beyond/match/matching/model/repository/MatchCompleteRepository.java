package com.beyond.match.matching.model.repository;

import com.beyond.match.matching.model.entity.MatchComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchCompleteRepository extends JpaRepository<MatchComplete, Integer> {
}
