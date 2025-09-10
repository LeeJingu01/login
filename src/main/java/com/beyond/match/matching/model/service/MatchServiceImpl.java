package com.beyond.match.matching.model.service;

import com.beyond.match.matching.model.dto.MatchRequestDto;
import com.beyond.match.matching.model.dto.MatchResponseDto;
import com.beyond.match.matching.model.entity.MatchApplication;
import com.beyond.match.matching.model.entity.MatchComplete;
import com.beyond.match.matching.model.repository.MatchCompleteRepository;
import com.beyond.match.matching.model.repository.MatchRepo;
import com.beyond.match.user.model.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final MatchRepo matchRepo;
    private final MatchRedisService matchRedisService;
    private final MatchCompleteRepository  matchCompleteRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final Map<String,Integer> CAPACITY = Map.of(
            "basketball", 10, "soccer", 10, "badminton", 4, "tennis", 2
    );

    @Override
    public MatchResponseDto saveMatch(MatchRequestDto requestDto, User applicant) {
        MatchApplication matchApplication = new MatchApplication();
        matchApplication.setMatchApplication(requestDto, applicant);

        MatchApplication savedMatch = matchRepo.save(matchApplication);

        MatchResponseDto matchResponseDto = new MatchResponseDto(savedMatch);

        addToMatchList(savedMatch);


        return matchResponseDto;
    }

    @Override
    public MatchApplication getMatch(int applicationId) {

        return matchRepo.findById(applicationId).orElse(null);
    }

    @Override
    @Transactional
    public void deleteMatch(int applicationId) {
        matchRepo.deleteById(applicationId);
        // set에서 applcationId랑 같은 id를 찾아 삭제
        MatchApplication matchApplication = matchRepo.findById(applicationId).orElse(null);
        String key = getMatchKey(matchApplication);
        String value = String.valueOf(matchApplication.getApplicantId().getUserId());
//        int deleteId = matchApplication.getApplicantId().getUserId();
//        if (deleteId == user.getUserId()) {
//            value = String.valueOf(deleteId);
//        }

        matchRedisService.removeFromSet(key, value);
    }

    @Override
    public List<MatchApplication> getMatches() {
        return matchRepo.findAll();
    }

    // Key: match:sport:region:date
    private String getMatchKey(MatchApplication dto) {
        int startH = Integer.parseInt(dto.getStart().split(":")[0]);
        int endH = Integer.parseInt(dto.getEnd().split(":")[0]);
        return String.format("match:%s:%s:%s:%d-%d",
                dto.getSport(), dto.getRegion(), dto.getMatchDate(),  startH, endH);
    }

    public void addToMatchList(MatchApplication matchApplication) {
        String key = getMatchKey(matchApplication);
        String value = String.valueOf(matchApplication.getApplicantId().getUserId());

        matchRedisService.addToSet(key, value);

        tryFinalize(key, matchApplication);
    }

    private void tryFinalize(String key, MatchApplication matchApplication) {
        String lockKey = key + ":lock";
        String token = UUID.randomUUID().toString();
        if(!acquire(lockKey, token, 3000)) return;

        try {
            long size = matchRedisService.getSetSize(key);
            int need = CAPACITY.getOrDefault(matchApplication.getSport(), 10);
            if(size<need) return;

            Set<String> picked = popN(key, need);
            if(picked.size() < need){
                if(!picked.isEmpty()){
                    redisTemplate.opsForSet().add(key, picked.toArray(new String[0]));
                }
                return;
            }
            int startH = Integer.parseInt(matchApplication.getStart().split(":")[0]);
            int endH = Integer.parseInt(matchApplication.getEnd().split(":")[0]);
            MatchComplete matchComplete = MatchComplete.builder()
                    .sport(matchApplication.getSport())
                    .region(matchApplication.getRegion())
                    .matchDate(matchApplication.getMatchDate())
                    .start(String.format("%02d:00", startH))
                    .end(String.format("%02d:00", endH))
                    .capacity(picked.size())
                    .build();
            MatchComplete saved = matchCompleteRepository.save(matchComplete);
            int matchPK = saved.getMatchId();

            if (matchRedisService.getSetSize(key) == 0) {
                redisTemplate.delete(key);
            }
        }finally {
            release(lockKey, token);
        }
    }

    private Set<String> popN(String key, int need) {
        Set<String> set = new LinkedHashSet<>();

        for (int i = 0; i < need; i++) {
            String v = redisTemplate.opsForSet().pop(key);
            if(v==null) break;
            set.add(v);
        }
        return set;
    }

    private boolean acquire(String key, String token, long ttlMs) {
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(key, token, Duration.ofMillis(ttlMs));
        return Boolean.TRUE.equals(ok);
    }
    private void release(String key, String token) {
        String cur = redisTemplate.opsForValue().get(key);
        if (token.equals(cur)) redisTemplate.delete(key);
    }
}
