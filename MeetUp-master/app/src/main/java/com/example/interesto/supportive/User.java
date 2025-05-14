package com.example.interesto.supportive;

public class User {

    private String uid,name,phoneNumber,profileImage,uabout,uinterest;

    public User(){

    }

    public User(String uid, String name, String phoneNumber, String profileImage, String uabout, String uinterest) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.uabout = uabout;
        this.uinterest = uinterest;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUabout() {
        return uabout;
    }

    public void setUabout(String uabout) {
        this.uabout = uabout;
    }

    public String getUinterest() {
        return uinterest;
    }

    public void setUinterest(String uinterest) {
        this.uinterest = uinterest;
    }
}
