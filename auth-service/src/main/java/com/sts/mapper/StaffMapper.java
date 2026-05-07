package com.sts.mapper;

import com.sts.dto.request.CreateStaffRequest;
import com.sts.dto.response.StaffResponse;
import com.sts.model.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public Staff toStaff(CreateStaffRequest request) {
        return Staff.builder()
                .name(request.name())
                .permissions(request.permissions())
                .address(request.address())
                .role(request.role())
                .contactNumber(request.contactNumber()).build();
    }

    public StaffResponse toResponse(Staff staff) {
        return StaffResponse.builder()
                .name(staff.getName())
                .address(staff.getAddress())
                .contactNumber(staff.getContactNumber())
                .permissions(staff.getPermissions())
                .role(staff.getRole())
                .build();
    }

}
