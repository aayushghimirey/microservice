package com.sts.utils.constant;

/**
 * Central place for all API-related string constants used across the Menu
 * Service.
 */
public interface AppConstants {



    // ── Base Paths ─────────────────────────────────────────────────────────
    String MENU_BASE_PATH = "/menus";

    // ── Error Messages ─────────────────────────────────────────────────────
    String MENU_NOT_FOUND = "Menu not found with id: ";
    String INVALID_INGREDIENT = "Invalid ingredient — variantId: %s, unitId: %s";
    String DUPLICATE_MENU_CODE = "A menu with code '%s' already exists";
     String DUPLICATE_MENU_NAME = "Menu with name '%s' already exits";

}
