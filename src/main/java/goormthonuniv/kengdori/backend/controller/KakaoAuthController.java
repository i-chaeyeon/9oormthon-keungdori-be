package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.KakaoCallbackDTO;
import goormthonuniv.kengdori.backend.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.service.KakaoAuthService;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    @GetMapping("/kakao-url")
    public ResponseEntity<?> passUrl(){
        return ResponseEntity.ok(Map.of(
                "url", kakaoAuthService.getKakaoLoginUrl()
        ));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoCallbackDTO> callback(@RequestParam String code){

        String token = kakaoAuthService.getKakaoAccessToken(code);
        Long kakaoId = kakaoAuthService.getKakaoId(token);
        boolean exists = userService.existsByKakaoId(kakaoId);
        String accessToken = jwtUtil.createAccessToken(kakaoId);

        if(exists){
            return ResponseEntity.status(HttpStatus.OK).body(new KakaoCallbackDTO(true, accessToken));
        }

        // 신규 사용자의 경우 DB에 임시 객체 생성
        String refreshToken = jwtUtil.createRefreshToken(kakaoId);
        userService.createTempUser(kakaoId, refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new KakaoCallbackDTO(false, accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        User user = userService.findUserByKakaoId(kakaoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue("refreshToken") String refreshToken) {
        if (!jwtUtil.isValidRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token expired"));
        }

        Long kakaoId = jwtUtil.getClaimsToken(refreshToken).get("kakaoId", Long.class);
        String newAccessToken = jwtUtil.createAccessToken(kakaoId);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}