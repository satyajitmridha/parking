package com.plabon.parking.model;

public class User {
    String id;
    String Companycode;
    String Branch;
    String name;
    String dob;
    String p;
    String mobile;
    String token;
    String status;



    public User() {
    }

    public User(String name, String dob, String mobile, String password) {
        this.name = name;
        this.dob = dob;
        this.mobile = mobile;
        this.p = password;
    }

    public User(String username, String password) {
        this.mobile = username;
        this.p = password;
    }

    public User(String id, String name, String username, String password, String mobile, String token) {
        this.id = id;
        this.name = name;
        this.dob = username;
        this.p = password;
        this.mobile = mobile;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getBrance() {
        return Branch;
    }

    public void setBrance(String brance) {
        this.Branch = brance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
