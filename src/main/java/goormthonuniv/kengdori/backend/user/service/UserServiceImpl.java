package goormthonuniv.kengdori.backend.user.service;

import goormthonuniv.kengdori.backend.user.DTO.UserRequestDTO;
import goormthonuniv.kengdori.backend.user.DTO.UserResponseDTO;
import goormthonuniv.kengdori.backend.user.domain.User;
import goormthonuniv.kengdori.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
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
            log.info("[수정] : 사용자 이름 변경 감지됨");
            user.setUserName(userRequestDTO.getUserName());
        }
        if(userRequestDTO.getSearchId() != null){
            user.setSearchId(userRequestDTO.getSearchId());
        }
        if(userRequestDTO.getSearch() != null){
            log.info("[수정] : 검색 가능 여부 변경 감지됨");
            user.setSearch(userRequestDTO.getSearch());
        }
        if(userRequestDTO.getKengColor() != null){
            log.info("[수정] : 킁도리 색상 변경 감지됨");
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
