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
    public void createTempUser(Long kakaoId, String refreshToken) {
        User user = User.builder()
                .kakaoId(kakaoId)
                .refreshToken(refreshToken)
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean existsByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId).isPresent();
    }

    @Override
    public boolean existsBySearchId(String searchId) {
        return userRepository.existsBySearchId(searchId);
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
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO, String accessToken) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if(userRequestDTO.getUserName() != null) {
            user.setUserName(userRequestDTO.getUserName());
        }
        if(userRequestDTO.getSearchId() != null){
            user.setSearchId(userRequestDTO.getSearchId());
        }
        if(userRequestDTO.getSearch() != null){
            user.setSearch(userRequestDTO.getSearch());
        }
        if(userRequestDTO.getKengColor() != null){
            user.setKengColor(userRequestDTO.getKengColor());
        }
        if(userRequestDTO.getProfileImage() != null){
            user.setProfileImage(userRequestDTO.getProfileImage());
        }

        return UserResponseDTO.from(user, accessToken);
    }

    @Override
    public User findUserByKakaoId(Long kakaoId){
        return userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(Long kakaoId){
        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRefreshToken(null);
    }
}
