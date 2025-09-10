package com.beyond.match.matching.model.entity;

import com.beyond.match.matching.model.dto.MatchRequestDto;
import com.beyond.match.user.model.vo.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class MatchApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matchApplicationId;

    @Column
    private String sport;

    @Column
    private String region;

    @Column
    private LocalDate matchDate;

    @Column
    private String preferredTime;

    @Column
    private String possibleTime;

    @Column
    private String genderOption;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicantId;

    private String start;
    private String end;

    public void setMatchApplication(MatchRequestDto matchRequestDto, User user) {
        this.sport = matchRequestDto.getSport();
        this.region = matchRequestDto.getRegion();
        this.matchDate = matchRequestDto.getMatchDate();
        this.preferredTime = matchRequestDto.getPreferredTime();
        this.possibleTime = matchRequestDto.getPossibleTime();
        this.genderOption = matchRequestDto.getGenderOption();
        this.createdAt = LocalDateTime.now();
        this.applicantId = user;

        this.start = matchRequestDto.getStart();
        this.end = matchRequestDto.getEnd();
    }
}
