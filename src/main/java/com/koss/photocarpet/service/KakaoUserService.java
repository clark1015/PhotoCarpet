package com.koss.photocarpet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koss.photocarpet.Security.JwtAuthenticationToken;
import com.koss.photocarpet.controller.dto.request.UserUpdateDto;
import com.koss.photocarpet.controller.dto.response.SocialUserResponse;
import com.koss.photocarpet.domain.user.CustomUserDetails;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserRepository;
import com.koss.photocarpet.Security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;


    public SocialUserResponse kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getAccessToken(code);

        SocialUserResponse kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfneed(kakaoUserInfo);

        forceLogin(kakaoUser);
        // Jwt 토큰을 해당 response 의 header에 붙여줄 방법은 무엇일까?
        SocialUserResponse userResponse = kakaoUserAuthorizationInput(kakaoUser, response);

        return userResponse;

    }

    public ResponseEntity<String> kakaoLogout(String userId, HttpServletRequest request) {
        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded");
        headers.add("Authorization", "KakaoAK " + "89eee4bc8a49d02a1847c5ed1e501e46");

        // HTTP 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", userId);

        HttpEntity httpEntity = new HttpEntity(body, headers);

        // HTTP 요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST, httpEntity, String.class);
        return response;
    }


    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "50b759fcb6b42d16b1e41ec1c6f47e58");
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        // HTTP 응답 (JSON) -> 액세스 토큰 생성
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info("get access_token " + jsonNode.get("access_token").asText());
        return jsonNode.get("access_token").asText();
    }

    private SocialUserResponse getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // access_token 으로 카카오 서버에서 해당 유저에 대한 responseBody 꺼냄.
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        log.info("get user info: " + jsonNode.get("kakao_account").get("email").asText());

        String email = "";
        Long id = jsonNode.get("id").asLong();
        if(jsonNode.get("kakao_account").get("has_email").asBoolean())
        {
            email = jsonNode.get("kakao_account").get("email").asText();

        }
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
//        log.info("get user info: " + email + " " + nickname);
        return new SocialUserResponse(id, nickname, email);
    }

    private User registerKakaoUserIfneed (SocialUserResponse kakaoUserInfo) {
        // 카카오 서버에서 받아온 유저 정보와 동일한 데이터가 어플 디비에 있는지 중복 확인
        String kakaoEmail = kakaoUserInfo.getEmail();
        String nickname = kakaoUserInfo.getNickname();
        User kakaoUser = userRepository.findByEmail(kakaoEmail);

        if (kakaoUser == null) {
            // DB 에 카카오 정보(카카오 이메일)와 매칭되는 것이 없다면 -> 새롭게 회원가입 시키기
            // password : random
//            String password = UUID.randomUUID().toString();
//            String encodedPassword = passwordEncoder.encode(password);

            String profile = "https://ossack.s3.ap-northeast-2.amazonaws.com/basicprofile.png";

            kakaoUser = User.builder().email(kakaoEmail).nickname(nickname).profileUrl(profile).build();
            userRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

    private Authentication forceLogin(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user.getUserId().toString(), user.getNickname(), user.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication: ", authentication);
        return authentication;
    }
    private SocialUserResponse kakaoUserAuthorizationInput(User user, HttpServletResponse response) {
        // response header 에 JWT token 추가
        String token = tokenProvider.create(user);
        SocialUserResponse responseUser = SocialUserResponse.builder().userId(user.getUserId()).nickname(user.getNickname()).email(user.getEmail()).token(token).build();
        return responseUser;
    }


    public SocialUserResponse updateUser(UserUpdateDto dto) {
        User user = userRepository.findById(dto.getUserId()).get();
        Optional<User> existingUser = userRepository.findByNickname(dto.getNickname());
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(dto.getUserId())) {
            log.info("Nickname already exists");
            throw new RuntimeException("Nickname already exists");
        }
        user.setNickname(dto.getNickname());
        user.setProfileUrl(dto.getProfileurl());
        userRepository.save(user);
        log.info("User information updated successfully");
        SocialUserResponse response = SocialUserResponse.builder().userId(user.getUserId()).email(user.getEmail()).nickname(user.getNickname()).build();
        return response;
    }

}




