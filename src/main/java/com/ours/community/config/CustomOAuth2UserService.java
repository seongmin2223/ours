package com.ours.community.config;

import com.ours.community.domain.user.Gender;
import com.ours.community.domain.user.User;
import com.ours.community.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("카카오 원본 데이터 출력: " + attributes);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Long providerId = (Long) attributes.get("id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (kakaoAccount == null) {
            System.out.println("에러 발생: kakao_account를 찾을 수 없습니다. 원본 데이터를 확인하세요.");
            throw new OAuth2AuthenticationException("카카오 계정 정보를 불러오는데 실패했습니다.");
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = (String) kakaoAccount.get("email");
        String nickname = profile != null ? (String) profile.get("nickname") : "이름없음";

        User user = saveOrUpdate(email, nickname, registrationId, String.valueOf(providerId));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                attributes,
                userNameAttributeName
        );
    }

    private User saveOrUpdate(String email, String nickname, String provider, String providerId) {
        Optional<User> findUser = userRepository.findByEmail(email);

        if (findUser.isPresent()) {
            return findUser.get();
        } else {
            User newUser = User.builder()
                    .email(email)
                    .nickname(nickname) // 카카오에서 낚아챈 닉네임 저장
                    .provider(provider)
                    .providerId(providerId)
                    .role("ROLE_USER")
                    .gender(Gender.UNKNOWN)
                    .build();

            System.out.println("[회원가입 완료] 새로운 카카오 유저가 DB에 정상 등록되었습니다. 이메일: " + email);
            return userRepository.save(newUser);
        }
    }
}