package com.plabon.parkingadmin.model;

public class CarTypeList {

    String id;
    String cartype;
    String des;

    public CarTypeList() {
    }

    public CarTypeList(String id, String cartype, String des) {
        this.id = id;
        this.cartype = cartype;
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
