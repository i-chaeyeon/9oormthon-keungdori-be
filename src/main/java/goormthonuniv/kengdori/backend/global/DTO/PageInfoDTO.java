package goormthonuniv.kengdori.backend.global.DTO;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfoDTO {
    private final int currentPage;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public PageInfoDTO(Page<?> page) {
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}