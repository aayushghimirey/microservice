package com.sts.controller;

import com.sts.dto.FinanceDashboardInfo;
import com.sts.enums.DateSelection;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.OverviewFinanceService;
import com.sts.utils.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(AppConstants.FINANCE_BASE_PATH)
public class FinanceInfoController {

    private final OverviewFinanceService overviewFinanceService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<FinanceDashboardInfo>> getInfo(DateSelection dateSelection) {
        return AppResponse.success(overviewFinanceService.financeDashboardInfo(dateSelection),
                AppConstants.SUCCESS_MESSAGES.FINANCE_DASHBOARD_FETCHED);
    }

}
