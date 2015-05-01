package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 08.04.2015.
 */
public class Calculation {
    static final String CALCULATION_TABLE = "calculations";

    static public final String CALC_ID = "_id";
    static public final String GROUPS_ID = "group_id";
    static public final String WHO_ID = "who_id";
    static public final String WHO_ID_VK = "who_id_vk";
    static public final String NAME_WHO = "who_name";
    static public final String WHOM_ID = "whom_id";
    static public final String WHOM_ID_VK = "whom_id_vk";
    static public final String NAME_WHOM = "whom_name";
    static public final String SUMMA = "summa";
    static public final String IS_DELETED = "is_deleted";
    public static final String STATE = "state";
    public static final String RESULT = "result";


    static public final String CREATE_TABLE = "create table " + CALCULATION_TABLE + "("
            + CALC_ID + " integer primary key, "
            + GROUPS_ID + " integer, "
            + WHO_ID + " integer, "
            + WHO_ID_VK + " integer, "
            + NAME_WHO + " char(70), "
            + WHOM_ID + " integer, "
            + WHOM_ID_VK + " integer, "
            + NAME_WHOM + " char(70), "
            + SUMMA + " integer, "
            + IS_DELETED + " integer, "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";

}