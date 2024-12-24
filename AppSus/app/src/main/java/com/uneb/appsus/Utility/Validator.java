package com.uneb.appsus.Utility;

public class Validator {
    public static boolean isNameValid(String name){
        if(!isTextMinSize(6, name)){
            return false;
        }

        if(!isTextMaxSize(100, name)){
            return false;
        }

        return true;
    }

    public static boolean isPhoneValid(String phone){
        if(!isTextMinSize(10, phone)){
            return false;
        }

        if(!isTextMaxSize(13, phone)){
            return false;
        }

        return true;
    }

    public static boolean isPasswordValid(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            return false;
        }

        if (!isTextMinSize(6, password)) {
            return false;
        }

        if (!isTextMaxSize(100, password)) {
            return false;
        }

        return true;
    }

    public static boolean isCPFValid(String cpf){
        if(cpf.length() != 11){
            return false;
        }

        return true;
    }

    public static boolean isSUSCardValid(String susCard){
        if(susCard.length() != 15){
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email){
        if(!isTextMinSize(6, email)){
            return false;
        }

        if(!isTextMaxSize(100, email)){
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isTextMinSize(int min, String text){
        return text.length() >= min;
    }

    public static boolean isTextMaxSize(int max, String text){
        return text.length() <= max;
    }
}
