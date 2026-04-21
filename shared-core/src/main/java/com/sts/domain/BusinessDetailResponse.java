package com.sts.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailResponse {

    private UUID id;
    private String companyName;
    private String address;
    private String panNumber;
    private String businessNumber;
    private String businessEmail;

}
