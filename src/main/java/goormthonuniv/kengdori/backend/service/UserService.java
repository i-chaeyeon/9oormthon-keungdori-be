package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;

public interface UserService {

    // C
    void createTempUser(Long kakaoId, String refreshToken);

    // R
    boolean existsByKakaoId(Long kakaoId);
    boolean existsByUserId(String userId);
    boolean canBeSearched();
    boolean isSubscribed();
    UserResponseDTO getMyProfile();
    User findUserByKakaoId(Long kakaoId);

    // U
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO, String accessToken);

    // D
    void deleteUser(Long id);
}
