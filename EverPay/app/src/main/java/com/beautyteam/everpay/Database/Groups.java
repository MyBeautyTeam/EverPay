package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Groups {
    static final String GROUPS_TABLE = "groups";

    static public final String GROUP_ID = "id";
    static private final String USER_ID = "user_id";
    static private final String TITLE = "title";

    static public final String CREATE_TABLE = "create table " + GROUPS_TABLE + "("
            + GROUP_ID + " integer primary key autoincrement, "
            + TITLE + " CHAR(50),"
            + USER_ID + " integer, "
            + "FOREIGN KEY(" + USER_ID + ") REFERENCES " + Users.USERS_TABLE + "(" + Users.USER_ID_VK + ")"
            + ")";

}
