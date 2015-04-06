package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class BillDetails {
    // Таблица
    static public final String BILL_DETAIL_TABLE = "bill_details";

    // Поля
    static public final String ITEM_ID = "_id";
    static public final String USER_ID = "user_id";
    static public final String DEBT_SUM = "debt_sum";
    static public final String INVEST_SUM = "invest_sum";
    static public final String BILL_ID = "bill_id";

    // Скрипт создания таблицы
    static public final String CREATE_TABLE = "create table " + BILL_DETAIL_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + USER_ID + " integer, "
            + DEBT_SUM + " integer,"
            + INVEST_SUM + " integer,"
            + BILL_ID + " integer,"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + Users.USERS_TABLE + "(" + Users.USER_ID_VK + ")"
            + "FOREIGN KEY(" + BILL_ID + ") REFERENCES " + Bills.BILLS_TABLE + "(" + Bills.BILL_ID + ")"
            + ")";
}
