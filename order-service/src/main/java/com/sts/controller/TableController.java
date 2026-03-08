package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.request.CreateTableCommand;
import com.sts.dto.response.TableResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.TableService;
import com.sts.utils.contant.AppConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(AppConstants.TABLE_BASE_PATH)
@AllArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<ApiResponse<TableResponse>> createTable(@RequestBody CreateTableCommand request) {
        log.info(AppConstants.LOG_MESSAGES.CREATING_TABLE, request.name());
        return AppResponse.success(tableService.createTable(request), AppConstants.SUCCESS_MESSAGES.TABLE_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<TableResponse>>> getAllTables(PageRequestDto pageRequestDto) {
        return AppResponse.success(tableService.getAllTables(pageRequestDto.buildPageable()),
                AppConstants.SUCCESS_MESSAGES.TABLES_FETCHED);
    }
}
