package com.example.basket.vo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WalletSqlLiter extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Wallet";
    public static final String PK_NO = "wallet_no";
    public static final String C_WALLET = "wallet";
    private static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + PK_NO + " INTEGER PRIMARY KEY,"
            + C_WALLET + " TEXT"
            + ")";
    private static final String DELETE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public WalletSqlLiter(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SQL);
        onCreate(db);
    }
}
