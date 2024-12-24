package com.uneb.appsus.DTO;

import java.io.Serializable;
import java.time.LocalTime;

public class HealthCenterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String address;

    private String openingHour;

    private String closingHour;
    
    private int availableAppointmentsCount;

    public int getAvailableAppointmentsCount() {
        return availableAppointmentsCount;
    }

    public void setAvailableAppointmentsCount(int availableAppointmentsCount) {
        this.availableAppointmentsCount = availableAppointmentsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalTime getOpeningHour() {
        return LocalTime.parse(openingHour);
    }

    public void setOpeningHour(LocalTime openingHour) {
        this.openingHour = openingHour.toString();
    }

    public LocalTime getClosingHour() {
        return LocalTime.parse(closingHour);
    }

    public void setClosingHour(LocalTime closingHour) {
        this.closingHour = closingHour.toString();
    }
}
