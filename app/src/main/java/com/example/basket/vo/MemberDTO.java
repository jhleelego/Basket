package com.example.basket.vo;

import blockchain.Wallet;

public class MemberDTO {
    public final String TAG = "MemberDTO";
    //private String  mem_entrance    = null;
    //private String  mem_email       = null;
    //private String  mem_pw          = null;

    private String  mem_entrance    = null;
    private String  mem_email       = null;
    private String  mem_pw          = null;
    private int     eve_code        = 0;

    private int     sto_code        = 0;
    private String  sto_name        = null;

    private int     mem_code        = 0;
    private String  mem_name        = null;
    private String  mem_gender      = null;
    private String  mem_age         = null;
    private String  mem_birth       = null;
    private String  mem_tel         = null;
    private Wallet  mem_wallet      = null;
    private String  mem_coin        = null;

    public MemberDTO(){
        MemberDTO.getInstance();
    }
    public static MemberDTO getInstance() {
        return MemberDTO.LazyHolder.instance;
    }
    private static class LazyHolder {
        private static final MemberDTO instance = new MemberDTO();
    }

    public String getTAG() {
        return TAG;
    }

    public int getEve_code() {
        return eve_code;
    }

    public void setEve_code(int eve_code) {
        this.eve_code = eve_code;
    }

    public String getMem_entrance() {
        return mem_entrance;
    }

    public void setMem_entrance(String mem_entrance) {
        this.mem_entrance = mem_entrance;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_pw() {
        return mem_pw;
    }

    public void setMem_pw(String mem_pw) {
        this.mem_pw = mem_pw;
    }

    public int getSto_code() {
        return sto_code;
    }

    public void setSto_code(int sto_code) {
        this.sto_code = sto_code;
    }

    public String getSto_name() {
        return sto_name;
    }

    public void setSto_name(String sto_name) {
        this.sto_name = sto_name;
    }

    public int getMem_code() {
        return mem_code;
    }

    public void setMem_code(int mem_code) {
        this.mem_code = mem_code;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_gender() {
        return mem_gender;
    }

    public void setMem_gender(String mem_gender) {
        this.mem_gender = mem_gender;
    }

    public String getMem_age() {
        return mem_age;
    }

    public void setMem_age(String mem_age) {
        this.mem_age = mem_age;
    }

    public String getMem_birth() {
        return mem_birth;
    }

    public void setMem_birth(String mem_birth) {
        this.mem_birth = mem_birth;
    }

    public String getMem_tel() {
        return mem_tel;
    }

    public void setMem_tel(String mem_tel) {
        this.mem_tel = mem_tel;
    }

    public Wallet getMem_wallet() {
        return mem_wallet;
    }

    public void setMem_wallet(Wallet mem_wallet) {
        this.mem_wallet = mem_wallet;
    }

    public String getMem_coin() {
        return mem_coin;
    }

    public void setMem_coin(String mem_coin) {
        this.mem_coin = mem_coin;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "TAG='" + TAG + '\'' +
                ", mem_entrance='" + mem_entrance + '\'' +
                ", mem_email='" + mem_email + '\'' +
                ", mem_pw='" + mem_pw + '\'' +
                ", eve_code=" + eve_code +
                ", sto_code=" + sto_code +
                ", sto_name='" + sto_name + '\'' +
                ", mem_code=" + mem_code +
                ", mem_name='" + mem_name + '\'' +
                ", mem_gender='" + mem_gender + '\'' +
                ", mem_age='" + mem_age + '\'' +
                ", mem_birth='" + mem_birth + '\'' +
                ", mem_tel='" + mem_tel + '\'' +
                ", mem_wallet=" + mem_wallet +
                ", mem_coin='" + mem_coin + '\'' +
                '}';
    }

    /***************************************************
     * DB BASKET member X member_detail Column !
     * mem_code
     * mem_email
     * mem_name
     * mem_pw
     * mem_age
     * mem_gender
     * mem_birth
     * mem_tel
     ****************************************************/

    //sto_code는 DB가 아닌 비콘으로 set 된다.


    public void removeInfo(){
        //setSto_code(null);
        setMem_entrance(null);
        setMem_code(0);
        setEve_code(0);
        setMem_email(null);
        setMem_pw(null);
        setMem_name(null);
        setMem_gender(null);
        setMem_age(null);
        setMem_birth(null);
        setMem_tel(null);
//        setMem_wallet(null);
        setMem_coin(null);
    }
}
