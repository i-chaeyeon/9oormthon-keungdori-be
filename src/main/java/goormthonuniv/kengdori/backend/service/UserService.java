package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;

public interface UserService {

    // C
    UserResponseDTO createUser(UserRequestDTO userRequestDTO, Long kakaoId);

    // R
    boolean existsByKakaoId(Long kakaoId);
    boolean existsByUserId(String userId);
    boolean canBeSearched();
    boolean isSubscribed();
    UserResponseDTO getMyProfile();
    UserResponseDTO findUserDtoByKakaoId(Long kakaoId);

    // U
    UserResponseDTO updateUser(UserRequestDTO userRequestDTO);

    // D
    void deleteUser();
}
