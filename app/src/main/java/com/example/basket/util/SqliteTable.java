package com.example.basket.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Iterator;
import java.util.List;

public class SqliteTable extends SQLiteOpenHelper {
    public static final String TYPE_INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";
    public static final String TYPE_INTEGER = " INTEGER";
    public static final String TYPE_TEXT = " TEXT";


    public static String wrapColumn(String columnName, String type) {
        return columnName + type;
    }

    private String title;
    private String createSql;
    private String deleteSql;

    public SqliteTable(Context context, String tableName, List<String> columnList) {
        super(context, tableName, null, 1);
        title = tableName;
        StringBuilder sb = new StringBuilder("CREATE TABLE " + tableName + " (");
        Iterator<String> it = columnList.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
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

    public Cursor select(String where) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(title);
        if (where != null) {
            sb.append(" WHERE ");
            sb.append(where);
        }
        sb.append(";");
        return getWritableDatabase().rawQuery(sb.toString(), null);
    }

    public long insert(ContentValues values) {
        return getWritableDatabase().insert(title, null, values);
    }

    public long update(ContentValues values, String where) {
        return getWritableDatabase().update(title, values, where, null);
    }

    public long delete(String where) {
        return getWritableDatabase().delete(title, where, null);
    }

    public long insertOrUpdate(ContentValues values, String key) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(title);
        sb.append(" WHERE ");
        sb.append(key);
        sb.append("=");
        sb.append(values.get(key));
        sb.append(";");
        Cursor c = getWritableDatabase().rawQuery(sb.toString(), null);
        if (c != null && c.getCount() > 0) {
            sb.setLength(0);
            sb.append(key);
            sb.append("=");
            sb.append(values.get(key));
            return update(values, sb.toString());
        } else {
            return insert(values);
        }
    }
}
