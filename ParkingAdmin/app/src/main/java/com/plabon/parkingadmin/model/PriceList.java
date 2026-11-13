package com.plabon.parkingadmin.model;

public class PriceList {
    String id;
    String Companycode;
    String Branch;
    String cartype;
    String loadtype;
    String price;
    String status;

    public PriceList() {
    }

    public PriceList(String id, String companycode, String branch, String cartype, String loadtype, String price, String status) {
        this.id = id;
        Companycode = companycode;
        Branch = branch;
        this.cartype = cartype;
        this.loadtype = loadtype;
        this.price = price;
        this.status = status;
    }

    public PriceList(String id, String cartype, String loadtype, String price) {
        this.id = id;
        this.cartype = cartype;
        this.loadtype = loadtype;
        this.price = price;
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

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getLoadtype() {
        return loadtype;
    }

    public void setLoadtype(String loadtype) {
        this.loadtype = loadtype;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
