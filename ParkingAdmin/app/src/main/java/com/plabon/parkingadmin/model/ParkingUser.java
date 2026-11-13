package com.plabon.parkingadmin.model;

public class ParkingUser {
    String id;
    String Companycode;
    String brance;
    String name;
    String dob;
    String p;
    String mobile;
    String status;



    public ParkingUser() {
    }


    public ParkingUser(String id, String name, String dob, String p, String mobile) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.p = p;
        this.mobile = mobile;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
