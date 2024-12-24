package com.uneb.appsus.DTO;

public class DoctorAppointment {
    private String id;
    private String patientId;
    private String doctorName;
    private String appointmentDateTime;
    private String specialtyName;
    private boolean isTomorrow;
    private boolean isFinalized;

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
}
