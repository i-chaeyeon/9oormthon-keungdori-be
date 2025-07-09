package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;

public interface UserService {

    // C
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    // R
    boolean existsByKakaoId(Long kakaoId);
    boolean existsByUserId(String userId);
    boolean canBeSearched();
    boolean isSubscribed();
    UserResponseDTO getMyProfile();

    // U
    UserResponseDTO updateUser(UserRequestDTO userRequestDTO);

    // D
    void deleteUser();
}
