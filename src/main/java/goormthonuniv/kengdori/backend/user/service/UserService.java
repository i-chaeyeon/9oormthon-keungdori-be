package goormthonuniv.kengdori.backend.user.service;

import goormthonuniv.kengdori.backend.user.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.user.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.user.domain.User;

public interface UserService {

    // C
    void createTempUser(Long kakaoId, String refreshToken);

    // R
    boolean existsByKakaoId(Long kakaoId);
    boolean existsBySearchId(String searchId);
    boolean canBeSearched();
    boolean isSubscribed();
    UserResponseDTO getMyProfile();
    User findUserByKakaoId(Long kakaoId);

    // U
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO, String accessToken);

    // D
    void deleteUser(Long id);
    void deleteRefreshToken(Long kakaoId);
}
