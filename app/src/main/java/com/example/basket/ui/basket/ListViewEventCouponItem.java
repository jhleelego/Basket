package com.example.basket.ui.basket;

class ListViewEventCouponItem {
    private int eve_code;
    private String eve_name;
    private String eve_des;
    private int discount_for;
    private int dis_price;

    public int getEve_code() {
        return eve_code;
    }

    public void setEve_code(int eve_code) {
        this.eve_code = eve_code;
    }

    public String getEve_name() {
        return eve_name;
    }

    public void setEve_name(String eve_name) {
        this.eve_name = eve_name;
    }

    public String getEve_des() {
        return eve_des;
    }

    public void setEve_des(String eve_des) {
        this.eve_des = eve_des;
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
        return "ListViewEventCouponItem{" +
                "eve_code=" + eve_code +
                ", eve_name='" + eve_name + '\'' +
                ", eve_des='" + eve_des + '\'' +
                ", discount_for=" + discount_for +
                ", dis_price=" + dis_price +
                '}';
    }
}