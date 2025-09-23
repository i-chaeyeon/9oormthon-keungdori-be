package goormthonuniv.kengdori.backend.auth.controller;

import goormthonuniv.kengdori.backend.auth.DTO.KakaoCallbackDTO;
import goormthonuniv.kengdori.backend.auth.service.KakaoAuthService;
import goormthonuniv.kengdori.backend.global.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    @GetMapping("/kakao-url")
    public ResponseEntity<?> passUrl(){
        log.info("[카카오 URL을 보냈습니다.]");
        return ResponseEntity.ok(Map.of(
                "url", kakaoAuthService.getKakaoLoginUrl()
        ));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoCallbackDTO> callback(@RequestParam String code){
        log.info("[전달된 인가 코드] : " + code);
        String token = kakaoAuthService.getKakaoAccessToken(code);
        Long kakaoId = kakaoAuthService.getKakaoId(token);
        boolean exists = userService.existsByKakaoId(kakaoId);
        String accessToken = jwtUtil.createAccessToken(kakaoId);
        String refreshToken = jwtUtil.createRefreshToken(kakaoId);

        if(!exists){
            userService.createTempUser(kakaoId, refreshToken);
        }
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // 'None'을 사용하려면 반드시 'true'
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();

        log.info("[ResponseCookie 생성 완료] {}", cookie.toString()); // 이 줄을 추가

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new KakaoCallbackDTO(exists, accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        userService.deleteRefreshToken(kakaoId);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(Map.of("message", "로그아웃 성공"));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue("refreshToken") String refreshToken)  {
        log.info("[Reissue] 전달받은 리프레시 토큰: {}", refreshToken);

        if (!jwtUtil.isValidRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "유효하지 않은 토큰"));
        }

        Long kakaoId = jwtUtil.getClaimsToken(refreshToken).get("kakaoId", Long.class);
        String newAccessToken = jwtUtil.createAccessToken(kakaoId);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}