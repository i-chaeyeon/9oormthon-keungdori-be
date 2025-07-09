package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.service.KakaoAuthService;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserServiceImpl userService;

    @GetMapping("/kakao-url")
    public String passUrl(){
        return "redirect:" + kakaoAuthService.getKakaoLoginUrl();
    }


    @GetMapping("/kakao/callback")
    public String callback(@RequestParam String code){

        String token = kakaoAuthService.getKakaoAccessToken(code);
        Long kakaoId = kakaoAuthService.getKakaoId(token);
        boolean exists = userService.existsByKakaoId(kakaoId);

        log.info("카카오 로그인 시도 - KakaoId: {}", kakaoId);

        if (exists) {
            // 이미 가입 → 홈으로
            return "redirect:/home";
        } else {
            // 신규 → 회원가입 페이지로
            return "redirect:/signup";
        }
    }
}