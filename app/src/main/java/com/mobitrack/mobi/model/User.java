package com.mobitrack.mobi.model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by Genius 03 on 1/21/2018.
 */

public class User implements Serializable{
    private String uid;
    private String name;
    private String phone;
    private String email;
    private String photoUri;
    private String companyName;
    private String address;
    private int isActive;
    private String token;
    private int isAdmin;

    public User() {
    }

    public User(String uid, String name, String photoUri, String companyName) {
        this.uid = uid;
        this.name = name;
        this.photoUri = photoUri;
        this.companyName = companyName;
    }

    public User(String uid,String email){
        this.uid = uid;
        this.email = email;
        this.name="";
        this.phone="";
        this.companyName="";
        this.address="";
        this.photoUri="";
        this.isActive=1;


    }

    public User(FirebaseUser firebaseUser){
        this.uid = firebaseUser.getUid();
        this.name = firebaseUser.getDisplayName();
        this.photoUri = firebaseUser.getPhotoUrl().toString();
        this.email = firebaseUser.getEmail();
        this.companyName = "";
        this.address ="";
        this.isActive=1;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
