package com.sts.client;

import java.util.List;
import java.util.UUID;

import com.sts.utils.constant.AppConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sts.event.MenuIngredientResponse;
import com.sts.exception.MenuIntegrationException;
import com.sts.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuGateway {

    private final MenuClient menuClient;

    public List<MenuIngredientResponse> getIngredientsOrThrow(UUID menuId) {
        log.info(AppConstants.LOG_MESSAGES.FETCHING_MENU_INGREDIENTS, menuId);

        try {
            ResponseEntity<ApiResponse<List<MenuIngredientResponse>>> response = menuClient
                    .getMenuIngredientsById(menuId);

            if (response.getBody() == null || response.getBody().getData() == null) {
                throw new MenuIntegrationException(
                        String.format(AppConstants.ERROR_MESSAGES.MENU_NOT_FOUND, menuId));
            }

            return response.getBody().getData();
        } catch (Exception e) {
            throw new MenuIntegrationException(
                    String.format(AppConstants.ERROR_MESSAGES.MENU_INTEGRATION_FAILED, menuId, e.getMessage()));
        }
    }
}
