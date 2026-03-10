package com.sts.service;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorService {

    VendorResponse createVendor(CreateVendorCommand command);

    Page<VendorResponse> getAllVendors(Pageable pageable);

}
