package com.sts.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppConstants.TABLE_BASE_PATH)
@AllArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<ApiResponse<TableResponse>> createTable(
            @RequestBody CreateTableCommand request) {


        TableResponse response = tableService.createTable(request);

        log.info("Request completed: createTable tableId={}",
                response.getId());

        return AppResponse.success(
                response,
                AppConstants.SUCCESS_MESSAGES.TABLE_CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<TableResponse>>> getAllTables(
            PageRequestDto pageRequestDto) {

        log.info("Request received: getAllTables page={} size={}",
                pageRequestDto.getPage(),
                pageRequestDto.getSize());

        var response =
                tableService.getAllTables(pageRequestDto.buildPageable());

        log.info("Request completed: getAllTables totalElements={}",
                response.getTotalElements());

        return AppResponse.success(
                response,
                AppConstants.SUCCESS_MESSAGES.TABLES_FETCHED);
    }
}