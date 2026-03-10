package com.sts.mapper;

import com.sts.dto.request.CreateVendorCommand;
import com.sts.dto.response.VendorResponse;
import com.sts.model.vendor.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorMapper {

    public Vendor buildVendor(CreateVendorCommand command) {
        Vendor vendor = new Vendor();
        vendor.setName(command.getName());
        vendor.setAddress(command.getAddress());
        vendor.setPanNumber(command.getPanNumber());
        vendor.setContactNumber(command.getContactNumber());

        return vendor;

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
