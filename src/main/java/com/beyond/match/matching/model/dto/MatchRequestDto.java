package com.beyond.match.matching.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDto {

    @JsonProperty("sport")
    private String sport;

    private String region;

    private LocalDate matchDate;

    private String preferredTime;

    private String possibleTime;

    private String genderOption;


    private String start;
    private String end;


}
