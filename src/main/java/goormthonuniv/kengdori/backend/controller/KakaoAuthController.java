package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/api/auth")
//public class KakaoAuthController {
//
//    private final KakaoAuthService kakaoAuthService;
//
//    @GetMapping("/kakao-url")
//    public String loginPage(Model model){
//        return kakaoAuthService.getKakaoLoginUrl();
//    }
//}


//백엔드 테스트용
@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/kakao")
    public String loginPage(Model model){
        String location = kakaoAuthService.getKakaoLoginUrl();
        model.addAttribute("location", location);
        return "login";
    }
}