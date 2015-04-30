package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 04.04.2015.
 */
public class GroupMembers {
    static final String GROUP_MEMBERS_TABLE = "group_members";

    static public final String ITEM_ID = "_id";
    static public final String GROUP_ID = "group_id";
    static public final String USER_ID = "user_id";
    static public final String USER_ID_VK = "user_id_vk";
    static public final String USER_NAME = "user_name";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    static public final String CREATE_TABLE = "create table " + GROUP_MEMBERS_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + GROUP_ID + " integer,"
            + USER_ID + " integer,"
            + USER_ID_VK + " integer,"
            + USER_NAME + " char(70), "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";

}
