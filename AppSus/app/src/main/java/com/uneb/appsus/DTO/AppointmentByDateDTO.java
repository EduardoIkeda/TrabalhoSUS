package com.uneb.appsus.DTO;

import java.io.Serializable;

public class AppointmentByDateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private DoctorDTO doctor;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DoctorDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDTO doctor) {
        this.doctor = doctor;
    }
}
