package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Users {
    static final String USERS_TABLE = "users";

    static public final String USER_ID_VK = "_id";
    static public final String NAME = "user_name";
    static public final String IMG = "img";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    static public final String CREATE_TABLE = "create table " + USERS_TABLE + "("
            + USER_ID_VK + " integer primary key, "
            + NAME + " char(70), "
            + IMG + " char(70), "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";
}
