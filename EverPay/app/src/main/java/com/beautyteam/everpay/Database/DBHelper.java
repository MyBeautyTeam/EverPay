package com.beautyteam.everpay.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 04.04.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "EverPayDatabase";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Users.CREATE_TABLE);
//        db.execSQL(Groups.CREATE_TABLE);
//        db.execSQL(GroupDetails.CREATE_TABLE);
//        db.execSQL(Bills.CREATE_TABLE);
//        db.execSQL(BillDetails.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }
}
