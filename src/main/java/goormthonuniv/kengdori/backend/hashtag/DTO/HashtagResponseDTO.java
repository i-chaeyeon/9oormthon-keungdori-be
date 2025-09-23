package goormthonuniv.kengdori.backend.hashtag.DTO;

import lombok.Builder;

@Builder
public class HashtagResponseDTO {

    public String hashtag; // 해시태그 내용
    public String backgroundColor; // 배경 색상
    public String fontColor; // 폰트 색상
    public String status; // 생성된건지 조회된건지 수정된건지

}
