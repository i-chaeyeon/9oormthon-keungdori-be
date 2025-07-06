package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.config.KakaoAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private KakaoAuthConfig kakaoAuthConfig;

    public String getKakaoLoginUrl(){
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                +kakaoAuthConfig.getClientId()
                +"&redirect_uri="
                +kakaoAuthConfig.getRedirectUri();
    }
}
