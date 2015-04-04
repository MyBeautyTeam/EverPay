package com.beautyteam.everpay.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.beautyteam.everpay.Constants;

/**
 * Created by Admin on 04.04.2015.
 */
public class BillDetails {
    // Таблица
    static private final String BILL_DETAIL_TABLE = "bill_details";

    // Поля
    static private final String ITEM_ID = "id";
    static private final String USER_ID = "user_id";
    static private final String DEBT_SUM = "DEBT_SUM";
    static private final String INVEST_SUM = "INVEST_SUM";
    static private final String BILL_ID = "bill_id";

    // Скрипт создания таблицы
    static public final String CREATE_TABLE = "create table " + BILL_DETAIL_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + USER_ID + " integer, "
            + DEBT_SUM + " integer,"
            + INVEST_SUM + " integer,"
            + BILL_ID + "integer,"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCE" + Users.USERS_TABLE + "(" + Users.USER_ID_VK + ")"
            + "FOREIGN KEY(" + BILL_ID + ") REFERENCE" + Bills.BILL_TABLE + "(" + Bills.BILL_ID + ")"
            + ")";
}
