package goormthonuniv.kengdori.backend.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoAuthConfig {
    private String clientId;
    private String redirectUri;
    private String KAKAO_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private String KAKAO_USER_URL_HOST = "https://kapi.kakao.com";
}
