package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Bills {
    static final String BILLS_TABLE = "bills";

    static public final String BILL_ID = "_id";
    static public final String TITLE = "title_id";
    static public final String USER_ID = "user_id";
    static public final String USER_NAME = "user_name";
    static public final String GROUP_ID = "group_id";
    static public final String NEED_SUM = "need";
    static public final String INVEST_SUM = "invest";

    static public final String CREATE_TABLE = "create table " + BILLS_TABLE + "("
            + BILL_ID + " integer primary key autoincrement, "
            + TITLE + " CHAR(50),"
            + USER_ID + " integer, "
            + USER_NAME + " char(70), "
            + GROUP_ID + " integer,"
            + NEED_SUM + " integer, "
            + INVEST_SUM + " integer "
            + ")";
}
