package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Bills {
    static final String BILLS_TABLE = "bills";

    static public final String BILL_ID = "bill_id";
    static private final String TITLE = "title_id";
    static private final String USER_ID = "user_id";
    static private final String GROUP_ID = "group_id";

    static public final String CREATE_TABLE = "create table " + BILLS_TABLE + "("
            + BILL_ID + " integer primary key autoincrement, "
            + TITLE + " CHAR(50),"
            + USER_ID + " integer, "
            + GROUP_ID + " integer,"
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + Users.USERS_TABLE + "(" + Users.USER_ID_VK + ")"
            + "FOREIGN KEY(" + GROUP_ID + ") REFERENCES " + Groups.GROUPS_TABLE + "(" + Groups.GROUP_ID + ")"
            + ")";
}
