package goormthonuniv.kengdori.backend.controller;

import goormthonuniv.kengdori.backend.global.JWT.JwtUtil;
import goormthonuniv.kengdori.backend.dto.place.BoundedPlaceListResponseDTO;
import goormthonuniv.kengdori.backend.dto.place.BoundedPlaceResponseDTO;
import goormthonuniv.kengdori.backend.service.place.PlaceService;
import goormthonuniv.kengdori.backend.domain.user.User;
import goormthonuniv.kengdori.backend.service.user.UserServiceImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
            @RequestParam BigDecimal north,
            @RequestParam BigDecimal south,
            @RequestParam BigDecimal east,
            @RequestParam BigDecimal west
//            @RequestParam(defaultValue = "0") int page
    ) {
        BigDecimal currentX = west.add(east).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
        BigDecimal currentY = south.add(north).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

        log.info("[nearme] authHeader={}, west={}, east={}, south={}, north={}, currentX={}, currentY={}",
                authHeader, west, east, south, north, currentX, currentY);

        User user = findUser(authHeader);

//        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        List<BoundedPlaceResponseDTO> places =
                placeService.getReviewedPlacesInBoundary(
                        user.getId(), west, east, south, north, currentX, currentY
                );

        log.info("[nearme] userId={}, place count={}", user.getId(), places.size());

        BoundedPlaceListResponseDTO response = BoundedPlaceListResponseDTO.builder()
                .userColor(user.getKengColor())
                .places(places)
                .build();

        return ResponseEntity.ok(response);
    }
}
