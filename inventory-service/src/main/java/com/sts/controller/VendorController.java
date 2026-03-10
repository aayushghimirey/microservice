package com.sts.controller;


import com.sts.dto.request.CreateVendorCommand;
import com.sts.pagination.PageRequestDto;
import com.sts.response.AppResponse;
import com.sts.service.VendorService;
import com.sts.utils.contant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(AppConstants.VENDOR_BASE_PATH)
@AllArgsConstructor
public class VendorController {

    private final VendorService vendorService;


    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody CreateVendorCommand command) {
        return AppResponse.success(vendorService.createVendor(command), "Done vendor");
    }

    @GetMapping
    public ResponseEntity<?> getAllVendors(PageRequestDto pageRequestDto) {
        return AppResponse.success(vendorService.getAllVendors(pageRequestDto.buildPageable()), "Done vendor");
    }

}
