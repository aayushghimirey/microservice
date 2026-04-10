package com.sts.utils.feign;

import com.sts.filter.TenantHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${app.security.gateway-secret}")
    private String gatewaySecret;

    @Override
    public void apply(RequestTemplate requestTemplate) {


        UUID tenantId = TenantHolder.getTenantId();
        if (tenantId != null) {
            requestTemplate.header("X-Tenant-Id", tenantId.toString());
        }

        requestTemplate.header("X-Gateway-Secret", gatewaySecret);

    }
}
