package com.plabon.parkingadmin.model;

public class User {
    String id;
    String Companycode;
    String brance;
    String name;
    String u;
    String p;
    String mobile;
    String token;



    public User() {
    }

    public User(String name, String username, String mobile, String password) {
        this.name = name;
        this.u = username;
        this.mobile = mobile;
        this.p = password;
    }

    public User(String username, String password) {
        this.u = username;
        this.p = password;
    }

    public User(String id, String name, String username, String password, String mobile, String token) {
        this.id = id;
        this.name = name;
        this.u = username;
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
        return brance;
    }

    public void setBrance(String brance) {
        this.brance = brance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
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
