package com.beyond.match.matching.model.dto;

import com.beyond.match.matching.model.entity.MatchApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponseDto {
    private int matchApplicationId;

    private String sport;

    private String region;

    private LocalDate matchDate;

    private String appliedTime;

    private String possibleTime;

    private String genderOption;

    private LocalDateTime createdAt;

    public MatchResponseDto(MatchApplication matchApplication) {
        this.matchApplicationId = matchApplication.getMatchApplicationId();
        this.sport = matchApplication.getSport();
        this.region = matchApplication.getRegion();
        this.matchDate = matchApplication.getMatchDate();
        this.appliedTime = matchApplication.getPreferredTime();
        this.possibleTime = matchApplication.getPossibleTime();
        this.genderOption = matchApplication.getGenderOption();
        this.createdAt = matchApplication.getCreatedAt();
    }
}
