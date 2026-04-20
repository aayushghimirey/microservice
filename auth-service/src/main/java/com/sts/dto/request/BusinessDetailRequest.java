package com.sts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusinessDetailRequest {

    @NotNull(message = "Company name is required")
    private String companyName;
    @NotNull(message = "Address is required")
    private String address;
    @NotNull(message = "PAN number is required")
    private String panNumber;
    @NotNull(message = "Business number is required")
    private String businessNumber;
    @NotNull(message = "Business email is required")
    private String businessEmail;


}
