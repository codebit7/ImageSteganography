package com.example.imagesteganographyapp;

public class UserModel {



    private String firstName,lastName,email,password,imageUrl, phone,public_id;

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public  UserModel(){}

   public UserModel(String firstName, String lastName, String email, String password,String phone)
    {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
