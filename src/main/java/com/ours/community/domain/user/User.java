package com.ours.community.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname; // 닉네임 필드 추가

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Column(nullable = false)
    private String role;

    private String provider;
    private String providerId;

    private LocalDateTime createdAt;

    @Builder
    public User(String email, String nickname, Gender gender, String role, String provider, String providerId) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
    }
}