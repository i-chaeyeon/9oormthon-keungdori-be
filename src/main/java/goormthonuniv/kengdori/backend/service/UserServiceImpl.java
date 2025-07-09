package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public boolean existsByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId).isPresent();
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean canBeSearched() {
        return false;
    }

    @Override
    public boolean isSubscribed() {
        return false;
    }

    @Override
    public UserResponseDTO getMyProfile() {
        return null;
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public void deleteUser() {

    }
}
