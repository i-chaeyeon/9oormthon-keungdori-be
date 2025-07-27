package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.DTO.HashtagRequestDTO;
import goormthonuniv.kengdori.backend.DTO.HashtagResponseDTO;
import goormthonuniv.kengdori.backend.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.service.HashtagService;
import goormthonuniv.kengdori.backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/hashtags")
public class HashtagController {

    private final JwtUtil jwtUtil;
    private final HashtagService hashtagService;
    private final UserServiceImpl userService;

    @PostMapping("")
    public ResponseEntity<HashtagResponseDTO> findOrCreate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody HashtagRequestDTO hashtagRequestDTO
    ){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        User user = userService.findUserByKakaoId(kakaoId);

        HashtagResponseDTO response = hashtagService.findOrCreateUserHashtag(user, hashtagRequestDTO.hashtag);
        if("exists".equals(response.status)){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(201).body(response);
        }
    }
}
