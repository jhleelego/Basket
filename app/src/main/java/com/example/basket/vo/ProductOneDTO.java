package com.example.basket.vo;

import java.io.Serializable;

public class ProductOneDTO implements Serializable {
    private String PRO_IMG = null;
    private String PRO_STOCK_EA = null;
    private String PRO_NAME = null;
    private String PRO_CODE = null;
    private String PRO_PRICE = null;
    public String getPRO_IMG() {
        return PRO_IMG;
    }

    public String getPRO_STOCK_EA() {
        return PRO_STOCK_EA;
    }

    public String getPRO_NAME() {
        return PRO_NAME;
    }

    public String getPRO_CODE() {
        return PRO_CODE;
    }

    public String getPRO_PRICE() {
        return PRO_PRICE;
    }

    public void setPRO_IMG(String PRO_IMG) {
        this.PRO_IMG = PRO_IMG;
    }

    public void setPRO_STOCK_EA(String PRO_STOCK_EA) {
        this.PRO_STOCK_EA = PRO_STOCK_EA;
    }

    public void setPRO_NAME(String PRO_NAME) {
        this.PRO_NAME = PRO_NAME;
    }

    public void setPRO_CODE(String PRO_CODE) {
        this.PRO_CODE = PRO_CODE;
    }

    public void setPRO_PRICE(String PRO_PRICE) {
        this.PRO_PRICE = PRO_PRICE;
    }


}
