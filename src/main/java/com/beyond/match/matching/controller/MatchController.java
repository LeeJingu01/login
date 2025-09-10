package com.beyond.match.matching.controller;

import com.beyond.match.jwt.auth.model.UserDetailsImpl;
import com.beyond.match.matching.model.dto.MatchRequestDto;
import com.beyond.match.matching.model.dto.MatchResponseDto;
import com.beyond.match.matching.model.entity.MatchApplication;
import com.beyond.match.matching.model.service.MatchService;
import com.beyond.match.user.model.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/match-service")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    // 매칭 신청
    @PostMapping("/match-applications")
    public ResponseEntity<MatchResponseDto> createMatch(@RequestBody MatchRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인된 유저 정보 가져오기
        User user = userDetails.getUser();

        MatchResponseDto matchResponseDto = matchService.saveMatch(requestDto, user);

        return ResponseEntity.status(HttpStatus.OK).body(matchResponseDto);
    }

    // 매칭 조회
    @GetMapping("/match-applications/{applicationId}")
    public ResponseEntity<MatchResponseDto> getMatch(@PathVariable("applicationId") int applicationId) {

        MatchApplication matchApplication = matchService.getMatch(applicationId);

        return ResponseEntity.status(HttpStatus.OK).body(new MatchResponseDto(matchApplication));
    }

    // 매칭 신청 취소(매칭 신청 삭제)
    @DeleteMapping("/match-applications/{applicationId}")
    public ResponseEntity<String> deleteMatch(@PathVariable("applicationId") int applicationId) {

        matchService.deleteMatch(applicationId);

        return ResponseEntity.status(HttpStatus.OK).body("Match has been deleted.");
    }

    // 매칭 신청 리스트 조회
    @GetMapping("/match-applications")
    public ResponseEntity<List<MatchApplication>> getMatchByMatchApplicationId() {
        List<MatchApplication> matchList = matchService.getMatches();

        return ResponseEntity.status(HttpStatus.OK).body(matchList);
    }

    // 매칭 중인 리스트 조회



    // 매칭 완료 리스트 조회


}
