package com.uneb.appsus.DTO;

public class AppointmentDisplayDTO {
    private int id;
    private String appointmentDateTime;
    private String appointmentStatus;
    private int patientId;
    private String doctorName;
    private String specialtyName;
    private String healthCenterName;
    private String healthCenterAddress;
    private boolean isTomorrow;
    private boolean isFinalized;

    public AppointmentDisplayDTO(int id, String appointmentDateTime, String appointmentStatus, int patientId, String doctorName, String specialtyName, String healthCenterName, String healthCenterAddress, boolean isTomorrow, boolean isFinalized) {
        this.id = id;
        this.appointmentDateTime = appointmentDateTime;
        this.appointmentStatus = appointmentStatus;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.specialtyName = specialtyName;
        this.healthCenterName = healthCenterName;
        this.healthCenterAddress = healthCenterAddress;
        this.isTomorrow = isTomorrow;
        this.isFinalized = isFinalized;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
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

    public boolean isTomorrow() {
        return isTomorrow;
    }

    public void setTomorrow(boolean tomorrow) {
        isTomorrow = tomorrow;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public void setFinalized(boolean finalized) {
        isFinalized = finalized;
    }
}