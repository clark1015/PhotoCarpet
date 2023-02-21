package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.request.UserUpdateDto;
import com.koss.photocarpet.controller.dto.response.SocialUserResponse;
import com.koss.photocarpet.domain.user.CustomUserDetails;
import com.koss.photocarpet.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController
{
    private final KakaoUserService kakaoUserService;

    // 클라이언트로부터 인가 코드, response 받아와서 백엔드에서 가입, 또는 로그인(동일하게) 실행.
    // 여기서 나온 결과 Response 에서 .getToken() 하여, 클라이언트에서 헤더에 다는 로직이 필요함.
    @GetMapping("/user/kakao/callback")
    public SocialUserResponse kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        System.out.println("Log in process");
        SocialUserResponse socialUserResponse = kakaoUserService.kakaoLogin(code, response);
        log.info("socialUserResponse", socialUserResponse);
        return socialUserResponse;
//        response.addHeader("Authorization", "BEARER " + socialUserResponse.getToken());
//        response.sendRedirect("/test");
    }



    @GetMapping("/user/kakao/logout")
    public String kakaoLogout(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        String userId = userDetails.getUsername();
        kakaoUserService.kakaoLogout(userId, request);
        return "logout work";
    }

    // 유저 정보 수정 -> 이메일 불가, 닉네임 가능, 프로필 url 가능.
    @PutMapping("/user/kakao/update")
    public String kakaoUserUpdate(@RequestBody UserUpdateDto dto ){
        SocialUserResponse response =  kakaoUserService.updateUser(dto);
        return "유저 정보 수정 완료" + response.toString();
    }

    @GetMapping("/test")
    public String testController(@AuthenticationPrincipal Principal principal)
    {
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return "Test site is working. User ID: " + userDetails.getUserId();
    }



}
