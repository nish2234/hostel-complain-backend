package com.vit.hostel.entity.enums;

public enum ComplaintCategory {
    PLUMBING("Plumbing Issues"),
    ELECTRICITY("Electrical Problems"),
    FURNITURE("Furniture Repair"),
    CLEANING("Cleaning & Hygiene"),
    INTERNET("Internet/WiFi Issues"),
    AC_FAN("AC/Fan Problems"),
    WATER_SUPPLY("Water Supply Issues"),
    PEST_CONTROL("Pest Control"),
    DOOR_WINDOW("Door/Window Issues"),
    OTHER("Other Issues");

    private final String displayName;

    ComplaintCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
