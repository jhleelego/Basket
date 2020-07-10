package com.example.basket.ui.basket;

class ListViewBasketItem {
    private String pro_img;
    private String pro_name ;
    private String desired_stock_count;
    private String pro_price;

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

    public String getDesired_stock_count() {
        return desired_stock_count;
    }

    public void setDesired_stock_count(String desired_stock_count) {
        this.desired_stock_count = desired_stock_count;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }
}