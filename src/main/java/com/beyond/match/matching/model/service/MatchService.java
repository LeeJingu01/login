package com.beyond.match.matching.model.service;

import com.beyond.match.matching.model.dto.MatchRequestDto;
import com.beyond.match.matching.model.dto.MatchResponseDto;
import com.beyond.match.matching.model.entity.MatchApplication;
import com.beyond.match.user.model.vo.User;

import java.util.List;

public interface MatchService {
    MatchResponseDto saveMatch(MatchRequestDto requestDto, User userId);

    MatchApplication getMatch(int applicationId);

    void deleteMatch(int applicationId);

    List<MatchApplication> getMatches();
}
