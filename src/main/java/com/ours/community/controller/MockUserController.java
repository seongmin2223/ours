package com.ours.community.controller;

import com.ours.community.domain.user.User;
import com.ours.community.domain.user.UserRepository;
import com.ours.community.dto.UserRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock/users")
@RequiredArgsConstructor
public class MockUserController {

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> registerMockUser(@RequestBody UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .gender(request.getGender())
                .role("ROLE_USER")
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("가짜 유저 가입 성공! ID : " + user.getId());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
