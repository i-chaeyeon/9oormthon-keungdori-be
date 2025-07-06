package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.config.KakaoAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class KakaoAuthController {

    private final KakaoAuthConfig kakaoAuthConfig;

    @GetMapping("/page")
    public String loginPage(Model model){
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                +kakaoAuthConfig.getClientId()
                +"&redirect_uri="
                +kakaoAuthConfig.getRedirectUri();
        model.addAttribute("location", location);

        return "login";
    }
}
