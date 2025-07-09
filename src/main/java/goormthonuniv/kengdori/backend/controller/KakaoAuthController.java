package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/kakao-url")
    public String passUrl(){
        return "redirect:" + kakaoAuthService.getKakaoLoginUrl();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam String code){
        String token = kakaoAuthService.getKakaoAccessToken(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}