package com.sts.controller;

import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.FinanceService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractFinanceController<R> {

    protected abstract FinanceService<R> getService();

    protected abstract String getSuccessMessage();


    public ResponseEntity<PagedResponse<List<R>>> getAllRecords(PageRequestDto pageRequest) {
        return AppResponse.success(getService().getAllRecords(pageRequest.buildPageable()), getSuccessMessage());
    }

}
