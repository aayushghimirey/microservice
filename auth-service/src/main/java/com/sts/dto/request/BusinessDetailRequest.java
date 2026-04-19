package com.sts.dto.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
public class BusinessDetailRequest {

    private String companyName;
    private String address;
    private String panNumber;
    private String businessNumber;
    private String businessEmail;
    private String tenantId;


}
