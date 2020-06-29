package com.example.basket.vo;

import android.util.Log;

import androidx.annotation.NonNull;

public class MemberDTO {
    private static String mem_entrance = "";
    private static String mem_code = "";
    private static String mem_email = "";
    private static String mem_pw = "";
    private static String mem_name = "";
    private static String mem_gender = "";
    private static String mem_age = "";
    private static String mem_birth = "";
    private static String mem_tel = "";

    public final String TAG = "MemberDTO";

    private MemberDTO(){
        MemberDTO.getInstance();
    }

    public static MemberDTO getInstance() {
        return MemberDTO.LazyHolder.instance;
    }
    private static class LazyHolder {
        private static final MemberDTO instance = new MemberDTO();
    }
    public String getMem_Entrance() {
        return mem_entrance;
    }

    public void setMem_Entrance(String mem_entrance) {
        this.mem_entrance = mem_entrance;
    }

    public String getMem_code() {
        return mem_code;
    }

    public void setMem_code(String mem_code) {
        this.mem_code = mem_code;
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

    @NonNull
    @Override
    public String toString() {
        if (getMem_Entrance() != null && getMem_Entrance().length() > 0) {
            Log.i(TAG, "mem_entrance : " + getMem_Entrance());
        } else if (getMem_code() != null && getMem_code().length() > 0) {
            Log.i(TAG, "mem_code : " + getMem_code());
        } else if (getMem_email() != null && getMem_email().length() > 0) {
            Log.i(TAG, "mem_email : " + getMem_email());
        } else if (getMem_pw() != null && getMem_pw().length() > 0) {
            Log.i(TAG, "mem_pw : " + getMem_pw());
        } else if (getMem_name() != null && getMem_name().length() > 0) {
            Log.i(TAG, "mem_name : " + getMem_name());
        } else if (getMem_gender() != null && getMem_gender().length() > 0) {
            Log.i(TAG, "mem_gender : " + getMem_gender());
        } else if (getMem_age() != null && getMem_age().length() > 0) {
            Log.i(TAG, "mem_age : " + getMem_age());
        } else if (getMem_birth() != null && getMem_birth().length() > 0) {
            Log.i(TAG, "mem_birth : " + getMem_birth());
        } else if (getMem_tel() != null && getMem_tel().length() > 0) {
            Log.i(TAG, "mem_tel : " + getMem_tel());
        }
        return null;
    }
}
