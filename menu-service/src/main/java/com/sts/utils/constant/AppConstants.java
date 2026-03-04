package com.sts.utils.constant;

/**
 * Central place for all API-related string constants used across the Menu
 * Service.
 */
public final class AppConstants {

    private AppConstants() {
    }

    // ── Base Paths ─────────────────────────────────────────────────────────
    public static final String MENU_BASE_PATH = "/menus";

    // ── Error Messages ─────────────────────────────────────────────────────
    public static final String MENU_NOT_FOUND = "Menu not found with id: ";
    public static final String INVALID_INGREDIENT = "Invalid ingredient — variantId: %s, unitId: %s";
    public static final String DUPLICATE_MENU_CODE = "A menu with code '%s' already exists";

}
