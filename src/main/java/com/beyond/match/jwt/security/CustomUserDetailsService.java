package com.beyond.match.jwt.security;

import com.beyond.match.jwt.auth.model.UserDetailsImpl;
import com.beyond.match.user.model.repository.UserRepository;
import com.beyond.match.user.model.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 나중에 또 다른 인증 방식 추가하면 그때 구현
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        User u = userRepository.findById(userId).orElseThrow(() ->
//                new  UsernameNotFoundException("User not found with userId: " + userId));
//        return new UserDetailsImpl(u);
//    }
//}
