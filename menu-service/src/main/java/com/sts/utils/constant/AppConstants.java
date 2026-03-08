package com.sts.utils.constant;

/**
 * Central place for all API-related string constants used across the Menu
 * Service.
 */
public interface AppConstants {

    // ── Base Paths ─────────────────────────────────────────────────────────
    String MENU_BASE_PATH = "/menus";

    interface SUCCESS_MESSAGES {
        String MENU_CREATED = "Menu created successfully";
        String MENU_FETCHED = "Menu fetched successfully";
        String MENUS_FETCHED = "Menus fetched successfully";
        String MENU_INGREDIENTS_FETCHED = "Menu ingredients fetched successfully";
    }

    interface ERROR_MESSAGES {
        String MENU_NOT_FOUND = "Menu not found with id: %s";
        String INVALID_INGREDIENT = "Invalid ingredient — variantId: %s, unitId: %s";
        String DUPLICATE_MENU_CODE = "A menu with code '%s' already exists";
        String DUPLICATE_MENU_NAME = "Menu with name '%s' already exists";
        String VALIDATION_FAILED = "Validation failed";
        String UNEXPECTED_ERROR = "An unexpected error occurred";
        String INVALID_STOCK_RESPONSE = "Received invalid response from inventory service for variantId: %s, unitId: %s";
    }

    interface VALIDATION_MESSAGES {
        String MENU_NAME_REQUIRED = "Menu name is required";
        String MENU_CATEGORY_REQUIRED = "Menu category is required";
        String PRICE_REQUIRED = "Price is required";
        String PRICE_POSITIVE = "Price must be a positive value";
        String INGREDIENTS_REQUIRED = "At least one ingredient is required";
        String VARIANT_ID_REQUIRED = "Variant ID is required";
        String UNIT_ID_REQUIRED = "Unit ID is required";
        String QUANTITY_POSITIVE = "Quantity must be a positive value";
    }

    interface LOG_MESSAGES {
        String CREATING_MENU = "Creating menu with category: {}";
        String MENU_CREATED = "Menu created successfully with id: {}";
        String FETCHING_ALL_MENUS = "Fetching all menus — page: {}, size: {}";
        String FETCHING_MENU_BY_ID = "Fetching menu with id: {}";
        String FETCHING_MENU_INGREDIENTS = "Fetching menu ingredients for id: {}";
    }
}
