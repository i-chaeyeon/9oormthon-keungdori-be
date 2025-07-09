package goormthonuniv.kengdori.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goormthonuniv.kengdori.backend.DTO.KakaoTokenResponseDTO;
import goormthonuniv.kengdori.backend.config.KakaoAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAuthConfig kakaoAuthConfig;

    public String getKakaoLoginUrl(){
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                +kakaoAuthConfig.getClientId()
                +"&redirect_uri="
                +kakaoAuthConfig.getRedirectUri();
    }

    public String getKakaoAccessToken(String code) {
        // 카카오톡 서버로 POST 요청 후 받은 응답을 response (DTO)에 저장
        KakaoTokenResponseDTO response = WebClient.create()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code&client_id=" + kakaoAuthConfig.getClientId()
                        + "&redirect_uri=" + kakaoAuthConfig.getRedirectUri()
                        + "&code=" + code)
                .retrieve()
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        // 로그인에 필요한 토큰만 리턴
        return response.getAccessToken();
    }

    public Long getKakaoId(String token){
        return WebClient.create().get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);
                        return root.get("id").asLong();
                    } catch (Exception e) {
                        throw new RuntimeException("Fail", e);
                    }
                })
                .block();
    }
}
