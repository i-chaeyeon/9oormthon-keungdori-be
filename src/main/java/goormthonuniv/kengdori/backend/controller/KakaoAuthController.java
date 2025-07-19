package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.service.KakaoAuthService;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserServiceImpl userService;

    @GetMapping("/kakao-url")
    public ResponseEntity<?> passUrl(){
        return ResponseEntity.ok(Map.of(
                "url", kakaoAuthService.getKakaoLoginUrl()
        ));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam String code){

        String token = kakaoAuthService.getKakaoAccessToken(code);
        Long kakaoId = kakaoAuthService.getKakaoId(token);
        boolean exists = userService.existsByKakaoId(kakaoId);

        if (exists) {
            // 이미 가입 → 홈으로
            return ResponseEntity.ok(Map.of(
                    "new_user", "false"
            ));
        } else {
            // 신규 → 회원가입 페이지로
            return ResponseEntity.ok(Map.of(
                    "new_user", "true"
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("login-user");

        if (user != null) {
            log.info("로그아웃: 세션 사용자 = {}", user);
            session.invalidate();
            return ResponseEntity.ok().build();
        } else {
            log.info("로그아웃 실패: 세션에 사용자 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}