package com.sts.api;


import com.sts.dto.TableResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.useCase.TableQueryUseCase;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table")
@AllArgsConstructor
public class TableQueryController {

    private final TableQueryUseCase tableQueryUseCase;

    @GetMapping
    public ResponseEntity<Page<TableResponse>> getAllTables(@ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(tableQueryUseCase.getAllTables(pageRequestDto.buildPageable()).map(
                TableResponse::fromEntity
        ));
    }

}
