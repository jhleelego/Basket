package com.example.basket.vo;

public class EventCouponOneDTO {
    private String eve_code;
    private int discount_for;
    private int dis_price;
    private String sto_name;

    public String getSto_name() {
        return sto_name;
    }

    public void setSto_name(String sto_name) {
        this.sto_name = sto_name;
    }

    public String getEve_code() {
        return eve_code;
    }

    public void setEve_code(String eve_code) {
        this.eve_code = eve_code;
    }

    public int getDiscount_for() {
        return discount_for;
    }

    public void setDiscount_for(int discount_for) {
        this.discount_for = discount_for;
    }

    public int getDis_price() {
        return dis_price;
    }

    public void setDis_price(int dis_price) {
        this.dis_price = dis_price;
    }

    @Override
    public String toString() {
        return "EventCouponOneDTO{" +
                "eve_code='" + eve_code + '\'' +
                ", discount_for=" + discount_for +
                ", dis_price=" + dis_price +
                ", sto_name='" + sto_name + '\'' +
                '}';
    }
}