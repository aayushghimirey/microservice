package com.sts.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.mapper.VendorMapper;
import com.sts.model.vendor.Vendor;
import com.sts.repository.VendorRepository;
import com.sts.service.VendorService;
import com.sts.utils.constant.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    /*
     * Commands
     */
    @Override
    @Transactional
    public VendorResponse createVendor(CreateVendorCommand command) {

        Vendor vendor = vendorMapper.buildVendor(command);

        return vendorMapper.toResponse(vendorRepository.save(vendor));

    }

    /*
     * Queries
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VendorResponse> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable).map(vendorMapper::toResponse);
    }

}
