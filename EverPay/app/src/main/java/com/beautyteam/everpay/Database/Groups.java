package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Groups {
    static final String GROUPS_TABLE = "groups";

    static public final String GROUP_ID = "_id";
    static public final String TITLE = "title";
    static public final String UPDATE_TIME = "update_time";
    static public final String IS_CALCULATED = "is_calculated";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    static public final String CREATE_TABLE = "create table " + GROUPS_TABLE + "("
            + GROUP_ID + " integer primary key autoincrement, "
            + TITLE + " CHAR(50),"
            + UPDATE_TIME + " char(30), "
            + IS_CALCULATED + " integer, "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";

}

