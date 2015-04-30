package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Bills {
    static final String BILLS_TABLE = "bills";

    static public final String ITEM_ID = "_id";
    static public final String BILL_ID = "bill_id";
    static public final String TITLE = "title";
    static public final String USER_ID_VK = "user_id_vk";
    static public final String USER_ID = "user_id";
    static public final String USER_NAME = "user_name";
    static public final String GROUP_ID = "group_id";
    static public final String NEED_SUM = "need";
    static public final String INVEST_SUM = "invest";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    static public final String CREATE_TABLE = "create table " + BILLS_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + BILL_ID + " integer, "
            + TITLE + " CHAR(50), "
            + USER_ID + " integer, "
            + USER_ID_VK + " integer, "
            + USER_NAME + " char(70), "
            + GROUP_ID + " integer,"
            + NEED_SUM + " integer, "
            + INVEST_SUM + " integer, "
            + STATE + " integer,"
            + RESULT + " integer"
            + ")";
}
