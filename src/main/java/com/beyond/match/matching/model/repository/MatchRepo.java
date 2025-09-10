package com.beyond.match.matching.model.repository;

import com.beyond.match.matching.model.entity.MatchApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepo extends JpaRepository<MatchApplication, Integer> {
}
