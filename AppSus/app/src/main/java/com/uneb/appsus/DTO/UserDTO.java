package com.uneb.appsus.DTO;

import androidx.annotation.NonNull;

public class UserDTO implements java.io.Serializable {
    private Long id;
    private String SUSCard;
    private String Address;
    private String Password;
    private String CPF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSUSCard() {
        return SUSCard;
    }

    public void setSUSCard(String SUSCard) {
        this.SUSCard = SUSCard;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", SUSCard='" + SUSCard + '\'' +
                ", Address='" + Address + '\'' +
                ", Password='" + Password + '\'' +
                ", CPF='" + CPF + '\'' +
                '}';
    }

    public void save(){
        //log user

        System.out.println("User saved: " + this.toString());
    }
}
