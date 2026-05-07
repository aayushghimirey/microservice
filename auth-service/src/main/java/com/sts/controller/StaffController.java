package com.sts.controller;


import com.sts.dto.request.CreateStaffRequest;
import com.sts.dto.response.StaffResponse;
import com.sts.pagination.PageRequestDto;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.response.PagedResponse;
import com.sts.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@RequestBody CreateStaffRequest request) {
        return AppResponse.success(staffService.createStaff(request), "Successfully created staff");
    }

    @GetMapping
    public ResponseEntity<PagedResponse<List<StaffResponse>>> getAllStaff(@ModelAttribute PageRequestDto pageRequestDto) {
        return AppResponse.success(staffService.getAllStaff(pageRequestDto.buildPageableNoSort()), "success fetched");
    }

}
