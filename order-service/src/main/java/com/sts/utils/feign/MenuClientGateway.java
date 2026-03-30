package com.sts.utils.feign;

import com.sts.event.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MenuClientGateway {

    private static final Logger log = LoggerFactory.getLogger(MenuClientGateway.class);
    private final MenuClient menuClient;

    public MenuResponse getMenuById(UUID menuId) {
        log.info("Fetching menu details for menuId: {}", menuId);
        return Objects.requireNonNull(menuClient.getMenuById(menuId).getBody()).getData();
    }

}
