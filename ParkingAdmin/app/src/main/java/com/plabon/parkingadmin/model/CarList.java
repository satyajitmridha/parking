package com.plabon.parkingadmin.model;

public class CarList {

    String carnumber;
    String cartype;
    String loadtype;
    String intimestamp;
    String indate;
    String outdate;
    String outtimestamp;
    String staydays;
    String amount;
    String collectedby;
    String statusnow;
    String rate;

    public CarList() {
    }

    public CarList(String carnumber, String cartype, String loadtype, String intimestamp, String indate, String outdate, String outtimestamp, String staydays, String amount, String collectedby, String statusnow, String rate) {
        this.carnumber = carnumber;
        this.cartype = cartype;
        this.loadtype = loadtype;
        this.intimestamp = intimestamp;
        this.indate = indate;
        this.outdate = outdate;
        this.outtimestamp = outtimestamp;
        this.staydays = staydays;
        this.amount = amount;
        this.collectedby = collectedby;
        this.statusnow = statusnow;
        this.rate = rate;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
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

    public String getIntimestamp() {
        return intimestamp;
    }

    public void setIntimestamp(String intimestamp) {
        this.intimestamp = intimestamp;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public String getOutdate() {
        return outdate;
    }

    public void setOutdate(String outdate) {
        this.outdate = outdate;
    }

    public String getOuttimestamp() {
        return outtimestamp;
    }

    public void setOuttimestamp(String outtimestamp) {
        this.outtimestamp = outtimestamp;
    }

    public String getStaydays() {
        return staydays;
    }

    public void setStaydays(String staydays) {
        this.staydays = staydays;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCollectedby() {
        return collectedby;
    }

    public void setCollectedby(String collectedby) {
        this.collectedby = collectedby;
    }

    public String getStatusnow() {
        return statusnow;
    }

    public void setStatusnow(String statusnow) {
        this.statusnow = statusnow;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
