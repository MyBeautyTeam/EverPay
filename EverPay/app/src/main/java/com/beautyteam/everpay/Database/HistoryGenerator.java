package com.beautyteam.everpay.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.beautyteam.everpay.Constants;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 18.05.2015.
 */
public class HistoryGenerator {
    private Context context;
    private String name = "";
    private int male = 0;
    private int id = 0;
    private String format = "yyyy-MM-dd HH:mm:ss";

    public HistoryGenerator(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
        name = sharedPreferences.getString(Constants.Preference.USER_NAME, "Я");
        male = sharedPreferences.getInt(Constants.Preference.MALE, 1);
        id = sharedPreferences.getInt(Constants.Preference.USER_ID, 0);
    }

    public String addBill(int billId) {
        Cursor cursor = context.getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_BILL, Bills.BILL_ID + "=" + billId, null, null);
        cursor.moveToFirst();
        ContentValues cv = new ContentValues();

        cv.put(History.GROUP_ID, cursor.getInt(cursor.getColumnIndex(Bills.GROUP_ID)));
        if (male == 0)
            cv.put(History.TEXT_DESCRIPTION, "добавил счет");
        else
            cv.put(History.TEXT_DESCRIPTION, "добавила счет");
        cv.put(History.TEXT_WHO, name);
        cv.put(History.BILL_ID, billId);
        cv.put(History.ACTION_DATETIME, getDate());
        cv.put(History.ACTION, 4);
        cv.put(History.USERS_ID_WHO, id);
        cv.put(History.TEXT_WHAT_WHOM,  "\"" +cursor.getString(cursor.getColumnIndex(Bills.TITLE))+"\"");
        cv.put(History.STATE, Constants.State.IN_PROCESS);

        Uri uri = context.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
        return uri.getLastPathSegment();
    }

    /*
    Выбираем максимальную дату, увеличиваем секунду на 1 и вставляем.
     */
    private String getDate() {
        /*
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Cursor maxCursor = context.getContentResolver().query(EverContentProvider.HISTORY_CONTENT_URI, new String [] {"MAX("+History.ACTION_DATETIME+")"}, null, null, null);
        maxCursor.moveToFirst();
        String max = maxCursor.getString(0);

        int length = max.length();
        String lastChar = max.substring(length-2);
        int lastDigit = Integer.parseInt(lastChar);
        lastDigit++;

        max = max.substring(0, length-2) + lastDigit;
        return max;
        */

        Cursor maxCursor = context.getContentResolver().query(EverContentProvider.HISTORY_CONTENT_URI, new String [] {"MAX("+History.ACTION_DATETIME+")"}, null, null, null);
        maxCursor.moveToFirst();
        String max = maxCursor.getString(0);

        int lastInt = max.lastIndexOf(":");
        String lastChar = max.substring(lastInt+1);
        int lastDigit = Integer.parseInt(lastChar);
        lastDigit++;

        max = max.substring(0, lastInt+1) + lastDigit;
        return max;

    }

    private static final String[] PROJECTION_BILL = new String[] {
            Bills.ITEM_ID,
            Bills.BILL_ID,
            Bills.TITLE,
            Bills.USER_ID,
            Bills.USER_ID_VK,
            Bills.USER_NAME,
            Bills.GROUP_ID,
            Bills.NEED_SUM,
            Bills.INVEST_SUM
    };
}
