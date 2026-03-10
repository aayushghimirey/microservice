package com.sts.integration.menu;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sts.event.MenuIngredientResponse;
import com.sts.exception.ResourceNotFoundException;
import com.sts.response.ApiResponse;
import com.sts.utils.feign.MenuClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGateway {

    private final MenuClient menuClient;

    public List<MenuIngredientResponse> getIngredientsOrThrow(UUID menuId) {
        log.debug("Fetching ingredients for menuId={}", menuId);

        try {
            ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> response = menuClient
                    .getMenuIngredientsById(menuId);

            if (response.getBody() == null || response.getBody().getData() == null) {
                throw new ResourceNotFoundException(
                        String.format("Menu not found or has no ingredients for menuId '%s'", menuId));
            }

            return response.getBody().getData();
        } catch (Exception e) {
            log.error("Failed to fetch menu ingredients for menuId={}", menuId, e);
            throw new ResourceNotFoundException(
                    String.format("Error fetching menu ingredients for menuId '%s': %s", menuId, e.getMessage()));
        }
    }
}
