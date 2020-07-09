package com.example.basket.vo;

public class ProductOneDTO {
    private String pro_img = null;
    private String pro_stock_ea = null;
    private String pro_name = null;
    private String pro_code = null;
    private String pro_price = null;

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getPro_stock_ea() {
        return pro_stock_ea;
    }

    public void setPro_stock_ea(String pro_stock_ea) {
        this.pro_stock_ea = pro_stock_ea;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_code() {
        return pro_code;
    }

    public void setPro_code(String pro_code) {
        this.pro_code = pro_code;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    @Override
    public String toString() {
        return "ProductOneDTO{" +
                "pro_img='" + pro_img + '\'' +
                ", pro_stock_ea='" + pro_stock_ea + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", pro_code='" + pro_code + '\'' +
                ", pro_price='" + pro_price + '\'' +
                '}';
    }
}