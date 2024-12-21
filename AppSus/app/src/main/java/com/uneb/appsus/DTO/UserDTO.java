package com.uneb.appsus.DTO;

import androidx.annotation.NonNull;

public class UserDTO implements java.io.Serializable {
    private Long id;
    private String susCardNumber;
    private String name;
    private String phone;
    private String email;
    private String Address;
    private String password;
    private String cpf;
    private final String role = "citizen";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSusCardNumber() {
        return susCardNumber;
    }

    public void setSusCardNumber(String susCardNumber) {
        this.susCardNumber = susCardNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", SUSCard='" + susCardNumber + '\'' +
                ", Address='" + Address + '\'' +
                ", Password='" + password + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void save(){
        //log user

        System.out.println("User saved: " + this.toString());
    }
}
