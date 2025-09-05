package goormthonuniv.kengdori.backend.DTO;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class ReadResponseDTO<T> {
    private final List<T> content;
    private final PageInfoDTO pageInfo;

    public ReadResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.pageInfo = new PageInfoDTO(page);
    }
}