package com.ours.community.config;

import com.ours.community.domain.user.User;
import com.ours.community.domain.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        //카카오 고유 식별자(id) 추출
        String providerId = String.valueOf(oAuth2User.getAttributes().get("id"));

        //DB에서 해당 유저 찾기
        User user = userRepository.findByEmail(extractEmail(oAuth2User))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        //JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole(), user.getNickname());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        //프론트엔드(리액트 등)로 토큰을 쿼리 스트링에 실어서 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth/redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String extractEmail(OAuth2User oAuth2User) {
        java.util.Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        if (kakaoAccount != null) {
            return (String) kakaoAccount.get("email");
        }
        return null;
    }
}