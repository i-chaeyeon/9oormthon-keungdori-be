package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.DTO.UserUpdateRequestDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("api/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/check-id")
    public ResponseEntity<?> checkUserId(@RequestParam String value){
        return ResponseEntity.ok(Map.of(
                "available", !userService.existsByUserId(value)
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody UserRequestDTO userRequestDTO, HttpSession session){
        Long kakaoId = (Long) session.getAttribute("tempKakaoId");

        UserResponseDTO response = userService.createUser(userRequestDTO, kakaoId);
        session.setAttribute("login-user", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyInfo(HttpSession session){
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("login-user");
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> editMyInfo(
            @RequestBody UserUpdateRequestDTO updateRequestDTO,
            HttpSession session
    ){
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("login-user");
        Long id = user.getId();
        UserResponseDTO updatedUser = userService.updateUser(id, updateRequestDTO);
        session.setAttribute("login-user", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(HttpSession session){
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("login-user");

        if (user != null) {
            log.info("회원 탈퇴: 사용자 = {}", user.getId());
            userService.deleteUser(user.getId());
            session.invalidate();
            return ResponseEntity.ok().build();
        } else {
            log.info("탈퇴 실패: 세션에 사용자 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
