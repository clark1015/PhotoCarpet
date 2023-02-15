package com.koss.photocarpet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koss.photocarpet.controller.dto.request.UserRequestDTO;
import com.koss.photocarpet.controller.dto.response.SocialUserResponse;
import com.koss.photocarpet.domain.user.CustomUserDetails;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserRepository;
import com.koss.photocarpet.Security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TokenProvider tokenProvider;


    public SocialUserResponse AppLogin(SocialUserResponse kakaoUser){
        forceLogin(kakaoUser);
        SocialUserResponse userResponse = kakaoUserAuthorizationInput(kakaoUser);
        return userResponse;

    }

    public SocialUserResponse kakaoSignup(UserRequestDTO kakaoUser){
        User user = User.builder().email(kakaoUser.getEmail()).nickname(kakaoUser.getNickname()).profileUrl(kakaoUser.getProfileurl()).profile_message(kakaoUser.getProfileMessage()).build();
        userRepository.save(user);
        SocialUserResponse response = SocialUserResponse.builder().email(kakaoUser.getEmail()).nickname(kakaoUser.getNickname()).profileUrl(kakaoUser.getProfileurl()).profileMessage(kakaoUser.getProfileMessage()).build();
        return AppLogin(response);
    }

    // 이후 클라이언트 단에서 localStorage.removeItem("jwtToken"); 세션에서 jwt 토큰을 지워주어야 한다.
    public ResponseEntity<String> kakaoLogout(String jwtToken, HttpServletRequest request) throws JsonProcessingException {
        // Decode the JWT token
        String[] tokenParts = jwtToken.split("\\.");
        String base64EncodedBody = tokenParts[1];

        // Decode the base64-encoded payload
        org.apache.commons.codec.binary.Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        // Parse the JSON string to get the access token
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String accessToken = jsonNode.get("access_token").asText();
        log.info("logout: accessToken:" + accessToken);

        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type",  "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP 바디 생성
        HttpEntity httpEntity = new HttpEntity(headers);

        // HTTP 요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST, httpEntity, String.class);
        return response;
    }


    public SocialUserResponse validateKakaoUser (UserRequestDTO kakaoUserInfo) {
        // 카카오 서버에서 받아온 유저 정보와 동일한 데이터가 어플 디비에 있는지 중복 확인
        String kakaoEmail = kakaoUserInfo.getEmail();
        User kakaoUser = userRepository.findByEmail(kakaoEmail);

        if (kakaoUser != null){
            return SocialUserResponse.builder().userId(kakaoUser.getUserId()).nickname(kakaoUser.getNickname())
                    .email(kakaoUser.getEmail()).accessToken(kakaoUserInfo.getAccessToken()).profileUrl(kakaoUser.getProfileUrl()).build();
        }
        else return null;

    }


    private Authentication forceLogin(SocialUserResponse user) {
        CustomUserDetails userDetails = new CustomUserDetails(user.getUserId().toString(), user.getNickname(), user.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication: userId={}, username={}, authorities={}",
                ((CustomUserDetails) authentication.getPrincipal()).getUserId(),
                ((CustomUserDetails) authentication.getPrincipal()).getUsername(),
                authentication.getAuthorities());
        return authentication;
    }

    // access_token 포함하여 Jwt token 만들기.
    // 클라이언트에서 이것을 받아 response header 에 JWT token 추가
    private SocialUserResponse kakaoUserAuthorizationInput(SocialUserResponse existingUser) {
        String accessToken = existingUser.getAccessToken();
        User user = User.builder().userId(existingUser.getUserId()).email(existingUser.getEmail()).nickname(existingUser.getNickname()).build();
        String JwtToken = tokenProvider.create(user, accessToken);
        SocialUserResponse responseUser = SocialUserResponse.builder().userId(user.getUserId()).nickname(user.getNickname()).email(user.getEmail()).jwtToken(JwtToken).build();
        return responseUser;
    }

    public SocialUserResponse updateUser(UserRequestDTO dto) {
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

    public SocialUserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).get();
        SocialUserResponse response = SocialUserResponse.builder().userId(user.getUserId()).email(user.getEmail()).nickname(user.getNickname()).profileUrl(user.getProfileUrl()).build();
        return response;
    }

}




