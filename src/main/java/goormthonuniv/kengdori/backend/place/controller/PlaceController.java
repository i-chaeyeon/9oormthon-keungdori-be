package goormthonuniv.kengdori.backend.place.controller;

import goormthonuniv.kengdori.backend.global.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.place.DTO.BoundedPlaceListResponseDTO;
import goormthonuniv.kengdori.backend.place.DTO.BoundedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.place.service.PlaceService;
import goormthonuniv.kengdori.backend.user.domain.User;
import goormthonuniv.kengdori.backend.user.service.UserServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {
    private final PlaceService placeService;
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    private User findUser(String authHeader){
        String accessToken = authHeader.replace("Bearer ", "");
        Long kakaoId = jwtUtil.getClaimsToken(accessToken).get("kakaoId", Long.class);
        return userService.findUserByKakaoId(kakaoId);
    }

    @GetMapping("/nearme")
    public ResponseEntity<BoundedPlaceListResponseDTO> getUserReviewedPlaces(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam BigDecimal minX,
            @RequestParam BigDecimal maxX,
            @RequestParam BigDecimal minY,
            @RequestParam BigDecimal maxY,
            @RequestParam BigDecimal currentX,
            @RequestParam BigDecimal currentY,
            @RequestParam(defaultValue = "0") int page // 사용하지는 않음
    ) {
        User user = findUser(authHeader);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending()); // 사용하지는 않음

        List<BoundedPlaceResponseDTO> places =
                placeService.getReviewedPlacesInBoundary(
                        user.getId(), minX, maxX, minY, maxY, currentX, currentY
                );

        BoundedPlaceListResponseDTO response = BoundedPlaceListResponseDTO.builder()
                .userColor(user.getKengColor())
                .places(places)
                .build();

        return ResponseEntity.ok(response);
    }
}
