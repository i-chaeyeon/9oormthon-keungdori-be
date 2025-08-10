package goormthonuniv.kengdori.backend.DTO;

import lombok.Builder;

@Builder
public class HashtagResponseDTO {

    public String hashtag; // 해시태그 내용
    public String color; // 색상
    public String status; // 생성된건지 조회된건지 수정된건지

}
