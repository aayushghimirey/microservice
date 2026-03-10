package com.sts.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateVendorCommand {
    private String name;
    private String address;
    private String contactNumber;
    private String panNumber;


}
