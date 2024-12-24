package com.uneb.appsus.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppointmentByDateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private final List<DoctorDTO> doctors = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DoctorDTO> getDoctorList() {
        return doctors;
    }
}
