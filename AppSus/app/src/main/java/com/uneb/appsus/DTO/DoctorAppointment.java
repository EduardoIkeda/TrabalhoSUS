package com.uneb.appsus.DTO;

import java.io.Serializable;

public class DoctorAppointment implements Serializable {
    private String id;
    private String patientId;
    private String doctorName;
    private String appointmentDateTime;
    private String specialtyName;
    private boolean isTomorrow;
    private boolean isFinalized;
    private String appointmentStatus;
    private Integer healthCenterId;
    private Integer specialtyId;
    private String healthCenterName;
    private String healthCenterAddress;

    public boolean isFinalized() {
        return isFinalized;
    }

    public void setFinalized(boolean finalized) {
        isFinalized = finalized;
    }

    public boolean isTomorrow() {
        return isTomorrow;
    }

    public void setTomorrow(boolean tomorrow) {
        isTomorrow = tomorrow;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getTime(){
        // "25/12/2024 10:30"
        return appointmentDateTime.split(" ")[1];
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Integer getHealthCenterId() {
        return healthCenterId;
    }

    public void setHealthCenterId(Integer healthCenterId) {
        this.healthCenterId = healthCenterId;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getHealthCenterName() {
        return healthCenterName;
    }

    public void setHealthCenterName(String healthCenterName) {
        this.healthCenterName = healthCenterName;
    }

    public String getHealthCenterAddress() {
        return healthCenterAddress;
    }

    public void setHealthCenterAddress(String healthCenterAddress) {
        this.healthCenterAddress = healthCenterAddress;
    }
}
