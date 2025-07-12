package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        session.setAttribute("user", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
