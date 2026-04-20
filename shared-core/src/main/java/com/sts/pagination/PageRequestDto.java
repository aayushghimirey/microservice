package com.sts.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    private int page = 0;
    private int size = 10;

    public Pageable buildPageable() {
        return PageRequest.of(page, size, Sort.by("createdDateTime").descending());
    }

    public Pageable buildPageable(String sortBy) {
        return PageRequest.of(page, size, Sort.by(sortBy).descending());
    }
}
