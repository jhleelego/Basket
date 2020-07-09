package com.example.basket.vo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqliToad extends SQLiteOpenHelper {
    public static final String TYPE_INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";
    public static final String TYPE_INTEGER = " INTEGER";
    public static final String TYPE_TEXT = " TEXT";

    class Column {
        private String query;

        public Column(@NotNull String columnName, @NotNull String type) {
            query = columnName + type;
        }

    }

    private String title;
    private String createSql;
    private String deleteSql;

    public SqliToad(@Nullable Context context, @NotNull String tableName, List<Column> columnList) {
        super(context, tableName, null, 1);
        title = tableName;
        StringBuilder sb = new StringBuilder("CREATE TABLE " + tableName + " (");
        Iterator<Column> it = columnList.iterator();
        while (it.hasNext()) {
            sb.append(it.next().query);
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(")");
        createSql = sb.toString();
        deleteSql = "DROP TABLE IF EXISTS " + tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteSql);
        onCreate(db);
    }

    public void resetTable() {
        onUpgrade(getWritableDatabase(), 1, 1);
    }

    public Cursor selectAll(String sql) {
        return getWritableDatabase().rawQuery("SELECT * FROM " + title + ";", null);
    }
    public boolean insertOrUpdate(String columnName, ContentValues values) {
        long rowId = getWritableDatabase().insert(title, null, values);
        if (rowId > 0) {
            return true;
        }
        return false;
    }
}
