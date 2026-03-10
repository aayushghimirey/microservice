package com.sts.service.impl;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.mapper.VendorMapper;
import com.sts.model.vendor.Vendor;
import com.sts.repository.VendorRepository;
import com.sts.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    public VendorResponse createVendor(CreateVendorCommand command) {

        Vendor vendor = vendorMapper.buildVendor(command);

        return vendorMapper.toResponse(vendorRepository.save(vendor));

    }

    @Override
    public Page<VendorResponse> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable).map(vendorMapper::toResponse);
    }


}
