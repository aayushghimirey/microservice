package com.sts.utils.feign;

import com.sts.event.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MenuClientGateway {

    private final MenuClient menuClient;

    public MenuResponse getMenuById(UUID menuId) {
        return Objects.requireNonNull(menuClient.getMenuById(menuId).getBody()).getData();
    }

}
