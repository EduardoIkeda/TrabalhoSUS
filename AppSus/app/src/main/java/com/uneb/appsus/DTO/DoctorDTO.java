package com.uneb.appsus.DTO;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String crm;

    private String startWork;

    private String endWork;

    private Set<DayOfWeek> workingDays = new HashSet<>();

    private List<DoctorAppointment> doctorAppointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getStartWork() {
        return startWork;
    }

    public void setStartWork(String startWork) {
        this.startWork = startWork;
    }

    public String getEndWork() {
        return endWork;
    }

    public void setEndWork(String endWork) {
        this.endWork = endWork;
    }

    public Set<DayOfWeek> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<DayOfWeek> workingDays) {
        this.workingDays = workingDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DoctorAppointment> getAppointments() {
        return doctorAppointments;
    }
}
