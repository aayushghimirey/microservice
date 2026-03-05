package com.sts.controller;

import com.sts.dto.request.CreateTableCommand;
import com.sts.dto.response.TableResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@AllArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<ApiResponse<TableResponse>> createTable(@RequestBody CreateTableCommand request) {
        return AppResponse.success(tableService.createTable(request), "Table create successfully");
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<TableResponse>>> getAllTables(@ModelAttribute PageRequestDto pageRequestDto) {
        return AppResponse.success(tableService.getAllTables(pageRequestDto.buildPageable()), "Success");
    }
}
