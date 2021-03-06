package com.beautyteam.everpay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * Created by Admin on 04.04.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "EverPayDatabase";
    public static final int DB_VERSION = 1;
    private static DBHelper sInstance;

    public static DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Users.CREATE_TABLE);
        db.execSQL(Groups.CREATE_TABLE);
        db.execSQL(Debts.CREATE_TABLE);
        db.execSQL(Calculation.CREATE_TABLE);
        db.execSQL(GroupMembers.CREATE_TABLE);
        db.execSQL(Bills.CREATE_TABLE);
        db.execSQL(History.CREATE_TABLE);
        db.execSQL(CalculationDetails.CREATE_TABLE);

        ContentValues cv = new ContentValues();

        for (int i = 1; i <= 15; i++) {
            cv.put(CalculationDetails.ITEM_ID, i);
            cv.put(CalculationDetails.GROUP_ID, i);
            cv.put(CalculationDetails.BILL_TITLE, "Счет за дружбу с пацанами" + i);
            cv.put(CalculationDetails.DEBT_SUM, new Random().nextInt(5000));
            cv.put(CalculationDetails.INVEST_SUM, new Random().nextInt(5000));
            cv.put(CalculationDetails.BALANCE, cv.getAsInteger(CalculationDetails.INVEST_SUM) - cv.getAsInteger(CalculationDetails.DEBT_SUM));
            db.insert(CalculationDetails.CALCULATION_DETAILS_TABLE, null, cv);
        }

        /*
        //for (int i = 1; i <= demo5; i++) {
            cv.put(Users.USER_ID_VK, i);
            cv.put(Users.NAME, "Lame ololo " + i);
            cv.put(Users.IMG, i + ".png");
            db.insert(Users.USERS_TABLE, null, cv);
        }
        for (int i = demo6; i <= 10; i++) {
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
        */

        // Groups
        /*
        for (int i = 1; i <= 10; i++) {
            cv = new ContentValues();
            cv.put(Groups.GROUP_ID, i);
            cv.put(Groups.TITLE, "group ololo " + i);
            cv.put(Groups.IS_CALCULATED, new Random().nextBoolean() ? 1:0);
            db.insert(Groups.GROUPS_TABLE, null, cv);
        }*/

        // Debts
        /*
        for (int i = 1; i <= 20; i++) {
            cv = new ContentValues();
            if (new Random().nextBoolean()) {
                cv.put(Debts.USER_ID_VK, i%10 + 1 );
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
        */


        // GroupMembers
        /*
        for (int i=0; i <= 10; i++) {
            cv = new ContentValues();
            cv.put(GroupMembers.GROUP_ID, i);
            int length = 10;
            for (int j=1; j<length; j++) {
                int id = j%10;
                cv.put(GroupMembers.USER_ID_VK, id);
                cv.put(GroupMembers.USER_NAME, "Name LastName" + id);
                db.insert(GroupMembers.GROUP_MEMBERS_TABLE, null, cv);
            }
        }
        */


        // Bills
        /*
        for (int i=1; i<=10; i++) {
            cv = new ContentValues();
            cv.put(Bills.BILL_ID, i);
            cv.put(Bills.TITLE, "bill #" + i);
            cv.put(Bills.GROUP_ID, i);
            int count = new Random().nextInt(10)+demo3;
            for (int j=1; j<count; j++) {
                cv.put(Bills.USER_ID_VK, j);
                cv.put(Bills.USER_NAME, "Name LastName" + j);
                int coef = new Random().nextInt(100);
                cv.put(Bills.INVEST_SUM, j * coef);
                cv.put(Bills.DEBT_SUM, j * coef);
                db.insert(Bills.BILLS_TABLE, null, cv);
            }
        }
        */

        // Calculation
        /*
        int calc_id = 0;

        for (int i=0; i<=10; i++) {
            cv = new ContentValues();
            cv.put(Calculation.GROUPS_ID, i);
            int count = new Random().nextInt(10) + demo2;
            for (int j=1; j<=count; j++) {
                int who = new Random().nextInt(10) + 1;
                cv.put(Calculation.WHO_ID, who);
                cv.put(Calculation.WHO_ID_VK, new Random().nextInt(100000));
                cv.put(Calculation.NAME_WHO, "Name LastName" + who);

                int whom = new Random().nextInt(10) + 1;
                cv.put(Calculation.WHOM_ID, whom);
                cv.put(Calculation.WHOM_ID_VK, new Random().nextInt(100000));
                cv.put(Calculation.NAME_WHOM, "Name LastName" + whom);

                cv.put(Calculation.SUMMA, new Random().nextInt(5000));
                cv.put(Calculation.IS_DELETED, new Random().nextBoolean()? 1:0);

                cv.put(Calculation.CALC_ID, calc_id);
                calc_id++;
                db.insert(Calculation.CALCULATION_TABLE, null, cv);
            }
        }
        */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

    }
}
