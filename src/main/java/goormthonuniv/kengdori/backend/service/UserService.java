package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.DTO.UserUpdateRequestDTO;

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
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userUpdateRequestDTO);

    // D
    void deleteUser(Long id);
}
