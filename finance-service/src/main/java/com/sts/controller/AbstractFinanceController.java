package com.sts.controller;

import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public abstract class AbstractFinanceController<R> {

    protected abstract FinanceService<R> getService();

    protected abstract String getSuccessMessage();

    @GetMapping
    public ResponseEntity<PagedResponse<List<R>>> getAllRecords(PageRequestDto pageRequest) {
        return AppResponse.success(getService().getAllRecords(pageRequest.buildPageable()), getSuccessMessage());
    }

}
