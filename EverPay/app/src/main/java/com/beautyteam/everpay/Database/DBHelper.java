package com.beautyteam.everpay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.User;

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
        db.execSQL(Groups.CREATE_TABLE);
        db.execSQL(GroupDetails.CREATE_TABLE);

        Log.d("SQL", Users.CREATE_TABLE + "\n" + Groups.CREATE_TABLE + "\n" + GroupDetails.CREATE_TABLE + "\n");

        ContentValues cv = new ContentValues();
        for (int i = 1; i <= 5; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "Lame ololo " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }
        for (int i = 6; i <= 10; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "Nlolo " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }
        for (int i = 11; i <= 15; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "Oolo " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }
        for (int i = 16; i <= 25; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "" + "PPPP " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }

        cv = new ContentValues();
        for (int i = 1; i <= 10; i++) {
            cv.put(Groups.GROUP_ID, i);
            cv.put(Groups.TITLE, "group ololo " + i);
            cv.put(Groups.USER_ID, i );
            db.insert(Groups.GROUPS_TABLE, null, cv);
        }

        cv = new ContentValues();
        for (int i=0; i <= 10; i++) {
            cv.put(GroupDetails.GROUP_ID, i);
            for (int j=0; j<25; j++) {
                cv.put(GroupDetails.USER_ID, j%10);
                db.insert(GroupDetails.GROUP_DETAILS_TABLE, null, cv);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }
}
