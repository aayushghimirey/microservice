package com.sts.service;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.request.GetVendorQueryRequest;
import com.sts.dto.request.UpdateVendorCommand;
import com.sts.dto.response.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    VendorResponse createVendor(CreateVendorCommand command);

    VendorResponse updateVendor(UUID vendorId, UpdateVendorCommand command);

    Page<VendorResponse> getAllVendors(GetVendorQueryRequest request, Pageable pageable);

}
