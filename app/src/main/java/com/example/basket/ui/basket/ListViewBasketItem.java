package com.example.basket.ui.basket;

class ListViewBasketItem {
    private int pro_code;
    private String pro_img;
    private String pro_name ;
    private int desired_stock_count;
    private int pro_price;

    public int getPro_code() {
        return pro_code;
    }

    public void setPro_code(int pro_code) {
        this.pro_code = pro_code;
    }

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public int getDesired_stock_count() {
        return desired_stock_count;
    }

    public void setDesired_stock_count(int desired_stock_count) {
        this.desired_stock_count = desired_stock_count;
    }

    public int getPro_price() {
        return pro_price;
    }

    public void setPro_price(int pro_price) {
        this.pro_price = pro_price;
    }

    @Override
    public String toString() {
        return "ListViewBasketItem{" +
                "pro_code=" + pro_code +
                ", pro_img='" + pro_img + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", desired_stock_count=" + desired_stock_count +
                ", pro_price=" + pro_price +
                '}';
    }
}