package com.example.ijp.drinkshop.Model;

public class User {
    private String phone;
    private String address;
    private String name;
    private String birthdate;
    private String err_msg;
    private String avatarurl;

    public User(String phone, String address, String name, String birthdate, String err_msg, String avatarurl) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.birthdate = birthdate;
        this.err_msg = err_msg;
        this.avatarurl = avatarurl;
    }

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }
}
