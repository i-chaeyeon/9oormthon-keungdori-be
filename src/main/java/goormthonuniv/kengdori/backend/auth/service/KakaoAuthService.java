package goormthonuniv.kengdori.backend.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goormthonuniv.kengdori.backend.auth.DTO.KakaoTokenResponseDTO;
import goormthonuniv.kengdori.backend.global.config.KakaoAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
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
        WebClient webClient = WebClient.create();

        log.info("========== 카카오 토큰 요청 시작 ==========");
        log.info("전달된 인가 코드: {}", code);
        log.info("요청 Redirect URI: {}", kakaoAuthConfig.getRedirectUri());
        log.info("요청 Client ID: {}", kakaoAuthConfig.getClientId());

        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoAuthConfig.getClientId())
                        .with("redirect_uri", kakaoAuthConfig.getRedirectUri())
                        .with("code", code))
                .exchangeToMono(response -> {
                    log.info("[카카오 응답 상태] {}", response.statusCode());

                    return response.bodyToMono(String.class)
                            .flatMap(body -> {
                                log.info("[카카오 응답 본문] {}", body);

                                if (response.statusCode().is2xxSuccessful()) {
                                    try {
                                        ObjectMapper mapper = new ObjectMapper();
                                        KakaoTokenResponseDTO dto = mapper.readValue(body, KakaoTokenResponseDTO.class);
                                        log.info("[액세스 토큰 발급 성공] {}", dto.getAccessToken());
                                        return Mono.just(dto);
                                    } catch (Exception e) {
                                        log.error("[카카오 토큰 응답 파싱 오류]", e);
                                        return Mono.error(e);
                                    }
                                } else {
                                    log.error("[카카오 토큰 요청 실패] 상태: {}, 응답: {}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("카카오 토큰 요청 실패: " + body));
                                }
                            });
                })
                .map(KakaoTokenResponseDTO::getAccessToken)
                .block();
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
