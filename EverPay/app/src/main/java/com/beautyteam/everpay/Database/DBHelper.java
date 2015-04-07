package com.beautyteam.everpay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.User;

import java.util.Random;

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
        db.execSQL(Debts.CREATE_TABLE);

        db.execSQL(GroupDetails.CREATE_TABLE);
        db.execSQL(Bills.CREATE_TABLE);
        db.execSQL(BillDetails.CREATE_TABLE);

        Log.d("SQL", Users.CREATE_TABLE + "\n" + Groups.CREATE_TABLE + "\n" + GroupDetails.CREATE_TABLE + "\n");

        // Users
        ContentValues cv = new ContentValues();
        for (int i = 1; i <= 10; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "name ololo " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }

        // Groups
        cv = new ContentValues();
        for (int i = 1; i <= 10; i++) {
            cv.put(Groups.GROUP_ID, i);
            cv.put(Groups.TITLE, "group ololo " + i);
            cv.put(Groups.USER_ID, i );
            db.insert(Groups.GROUPS_TABLE, null, cv);
        }

        // Debts
        for (int i = 1; i <= 20; i++) {
            cv = new ContentValues();
            if (new Random().nextBoolean()) {
                cv.put(Debts.USER_ID, i%10 + 1 );
                cv.put(Debts.USER_NAME, "USER №" + i);
            }

            cv.put(Debts.GROUP_TITLE, "group №" + i);
            cv.put(Debts.SUMMA, new Random().nextInt(1000));
            if (new Random().nextBoolean()) {
                cv.put(Debts.IS_I_DEBT, 0);
            } else {
                cv.put(Debts.IS_I_DEBT, 1);
            }
            db.insert(Debts.DEBTS_TABLE, null, cv);
        }


        // GroupDetails
        for (int i=0; i <= 10; i++) {
            cv = new ContentValues();
            cv.put(GroupDetails.GROUP_ID, i);
            int length = new Random().nextInt(7)+4;
            for (int j=0; j<length; j++) {
                cv.put(GroupDetails.USER_ID, j%10);
                db.insert(GroupDetails.GROUP_DETAILS_TABLE, null, cv);
            }
        }


        // Bills
        cv = new ContentValues();
        for (int i=0; i<=10; i++) {
            cv.put(Bills.BILL_ID, i);
            cv.put(Bills.TITLE, "bill #" + i);
            cv.put(Bills.USER_ID, i);
            cv.put(Bills.GROUP_ID, i);
            db.insert(Bills.BILLS_TABLE, null, cv);
        }

        // BillDetails
        cv = new ContentValues();
        for (int i=0; i<=10; i++) {
            cv.put(BillDetails.BILL_ID, i);
            for (int j=0; j<=4; j++) {
                cv.put(BillDetails.USER_ID, j);
                cv.put(BillDetails.DEBT_SUM, j);
                cv.put(BillDetails.INVEST_SUM, j);
                db.insert(BillDetails.BILL_DETAIL_TABLE, null, cv);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }
}
