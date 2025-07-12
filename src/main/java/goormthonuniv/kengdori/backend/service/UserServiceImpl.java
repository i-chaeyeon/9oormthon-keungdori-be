package goormthonuniv.kengdori.backend.service;

import goormthonuniv.kengdori.backend.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.domain.User;
import goormthonuniv.kengdori.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO, Long kakaoId) {
        User user = userRequestDTO.toUser(kakaoId);
        return UserResponseDTO.from(userRepository.save(user));
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
    public UserResponseDTO findUserDtoByKakaoId(Long kakaoId){
        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponseDTO.from(user);
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public void deleteUser() {

    }
}
