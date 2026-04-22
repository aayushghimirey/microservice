package com.sts.service.impl;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.request.GetVendorQueryRequest;
import com.sts.dto.request.UpdateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.exception.DuplicateResourceException;
import com.sts.filter.TenantHolder;
import com.sts.mapper.VendorMapper;
import com.sts.model.vendor.Vendor;
import com.sts.repository.VendorRepository;
import com.sts.service.VendorService;
import com.sts.service.specification.VendorSpecification;
import io.github.aayushghimirey.jpa_postgres_rls.core.RlsContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final RlsContext rlsContext;
    private final VendorSpecification vendorSpecification;

    /*
     * Commands
     */
    @Override
    @Transactional
    public VendorResponse createVendor(CreateVendorCommand command) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        assertVendorNameUnique(command.name());

        Vendor vendor = vendorMapper.buildVendor(command);

        return vendorMapper.toResponse(vendorRepository.save(vendor));

    }

    @Override
    @Transactional
    public VendorResponse updateVendor(UUID vendorId, UpdateVendorCommand command) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Vendor existingVendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + vendorId));

        if (!existingVendor.getName().equals(command.name())) {
            assertVendorNameUnique(command.name());
        }

        existingVendor.setName(command.name());

        if (command.address() != null) {
            existingVendor.setAddress(command.address());
        }
        if (command.contactNumber() != null) {
            existingVendor.setContactNumber(command.contactNumber());
        }
        if (command.panNumber() != null) {
            existingVendor.setPanNumber(command.panNumber());
        }

        return vendorMapper.toResponse(vendorRepository.save(existingVendor));
    }


    /*
     * Queries
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VendorResponse> getAllVendors(GetVendorQueryRequest vendorQueryRequest, Pageable pageable) {
        rlsContext.with("app.tenant_id", TenantHolder.getTenantId()).apply();

        Specification<Vendor> spec = vendorSpecification.buildSpecification(vendorQueryRequest);

        return vendorRepository.findAll(spec, pageable).map(vendorMapper::toResponse);
    }


    // ------ private helpers --------
    private void assertVendorNameUnique(String name) {
        if (vendorRepository.existsByName(name)) {
            log.warn("Vendor with name '{}' already exists.", name);
            throw new DuplicateResourceException("Vendor with name '" + name + "' already exists.");
        }
    }


}
