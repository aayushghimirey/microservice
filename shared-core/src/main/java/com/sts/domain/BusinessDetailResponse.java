package com.sts.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BusinessDetailResponse {

    private UUID id;
    private String companyName;
    private String address;
    private String panNumber;
    private String businessNumber;
    private String businessEmail;

}
