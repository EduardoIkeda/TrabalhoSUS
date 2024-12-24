package com.uneb.appsus.enums;

public enum AppointmentStatus {
    PENDING("pending"),
    SCHEDULED("scheduled"),
    ATTENDED("attended"),
    MISSED("missed"),
    CANCELED("canceled");

    private final String value;

    private AppointmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
