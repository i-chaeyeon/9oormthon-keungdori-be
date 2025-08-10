package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/check-id")
    public ResponseEntity<?> checkUserId(@RequestParam String value){
        return ResponseEntity.ok(Map.of(
                "available", !userService.existsBySearchId(value)
        ));
    }

    @PatchMapping("/signup")
    public ResponseEntity<UserResponseDTO> signUp(
            @RequestBody UserRequestDTO userRequestDTO,
            @RequestHeader("Authorization") String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);

        User user = userService.findUserByKakaoId(kakaoId);
        UserResponseDTO responseDTO = userService.updateUser(user.getId(), userRequestDTO, accessToken);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyInfo(@RequestHeader("Authorization") String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        UserResponseDTO user = UserResponseDTO.from(userService.findUserByKakaoId(kakaoId), accessToken);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> editMyInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserRequestDTO userRequestDTO
    ){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        UserResponseDTO updatedUser = userService.updateUser(userService.findUserByKakaoId(kakaoId).getId(), userRequestDTO, accessToken);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser( @RequestHeader("Authorization") String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        User user = userService.findUserByKakaoId(kakaoId);
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 성공"));
    }
}
