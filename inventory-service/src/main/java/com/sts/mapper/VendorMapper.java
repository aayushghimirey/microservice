package com.sts.mapper;

import org.springframework.stereotype.Component;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.model.vendor.Vendor;

@Component
public class VendorMapper {

    public Vendor buildVendor(CreateVendorCommand command) {
        return Vendor.builder()
                .name(command.name())
                .address(command.address())
                .contactNumber(command.contactNumber())
                .panNumber(command.panNumber())
                .build();
    }

    public VendorResponse toResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .contactNumber(vendor.getContactNumber())
                .address(vendor.getAddress())
                .panNumber(vendor.getPanNumber())
                .build();

    }

}
