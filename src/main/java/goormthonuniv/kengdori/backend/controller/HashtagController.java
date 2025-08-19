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

    private User findUser(String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        return userService.findUserByKakaoId(kakaoId);
    }

    // [1.리뷰등록] 1-1. 해시태그 조회 후 없을 시 생성
    @PostMapping("")
    public ResponseEntity<HashtagResponseDTO> findOrCreate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody HashtagRequestDTO hashtagRequestDTO
    ){
        User user = findUser(authHeader);

        HashtagResponseDTO response = hashtagService.findOrCreateUserHashtag(user, hashtagRequestDTO.hashtag);
        if("exists".equals(response.status)){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(201).body(response);
        }
    }

    // [1.리뷰등록] 1-2. 해시태그 색상 변경
    @PatchMapping("")
    public ResponseEntity<HashtagResponseDTO> changeColor(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody HashtagRequestDTO hashtagRequestDTO
    ){
        User user = findUser(authHeader);

        HashtagResponseDTO response = hashtagService.changeColor(user, hashtagRequestDTO);
        return ResponseEntity.ok().body(response);
    }
}
