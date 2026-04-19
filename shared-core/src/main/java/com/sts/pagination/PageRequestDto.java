package com.sts.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDto {

    private int page = 0;
    private int size = 10;

    public Pageable buildPageable() {
        return PageRequest.of(page, size, org.springframework.data.domain.Sort.by("createAt").descending());
    }

    public Pageable buildPageable(String sortBy) {
        return PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy).descending());
    }
}
