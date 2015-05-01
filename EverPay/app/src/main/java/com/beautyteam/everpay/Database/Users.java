package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class Users {
    static final String USERS_TABLE = "users";

    static public final String USER_ID = "_id";
    static public final String USER_ID_VK = "vk_id";
    static public final String NAME = "user_name";
    static public final String IMG = "img";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    static public final String CREATE_TABLE = "create table " + USERS_TABLE + "("
            + USER_ID + " integer primary key autoincrement, "
            + USER_ID_VK + " integer, "
            + NAME + " char(70), "
            + IMG + " char(70), "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";
}
