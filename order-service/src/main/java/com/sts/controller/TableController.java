package com.sts.controller;

import com.sts.dto.request.TableRequest;
import com.sts.dto.response.TableResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/table")
@AllArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<TableResponse> createTable(@RequestBody TableRequest request) {
        var response = tableService.createTable(request);
        return ResponseEntity.ok(TableResponse.fromEntity(response));
    }

    @GetMapping
    public ResponseEntity<Page<TableResponse>> getAllTables(@ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(tableService.getAllTables(pageRequestDto.buildPageable()).map(
                TableResponse::fromEntity));
    }
}
