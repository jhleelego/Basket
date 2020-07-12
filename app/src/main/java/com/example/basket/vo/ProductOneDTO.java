package com.example.basket.vo;

public class ProductOneDTO {
    private int pro_code = 0;
    private String pro_img = null;
    private int pro_stock_ea = 0;
    private String pro_name = null;
    private int pro_price = 0;

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

    public int getPro_stock_ea() {
        return pro_stock_ea;
    }

    public void setPro_stock_ea(int pro_stock_ea) {
        this.pro_stock_ea = pro_stock_ea;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public int getPro_price() {
        return pro_price;
    }

    public void setPro_price(int pro_price) {
        this.pro_price = pro_price;
    }

    @Override
    public String toString() {
        return "ProductOneDTO{" +
                "pro_code=" + pro_code +
                ", pro_img='" + pro_img + '\'' +
                ", pro_stock_ea=" + pro_stock_ea +
                ", pro_name='" + pro_name + '\'' +
                ", pro_price=" + pro_price +
                '}';
    }
}