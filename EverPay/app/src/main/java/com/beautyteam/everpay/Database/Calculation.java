package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 08.04.2015.
 */
public class Calculation {
    static final String CALCULATION_TABLE = "calculations";

    static public final String ITEM_ID = "_id";
    static public final String GROUPS_ID = "group_id";
    static public final String ID_WHO = "who_id";
    static public final String NAME_WHO = "who_name";
    static public final String ID_WHOM = "whom_id";
    static public final String NAME_WHOM = "whom_name";
    static public final String SUMMA = "summa";
    static public final String IS_DELETED = "is_deleted";

    static public final String CREATE_TABLE = "create table " + CALCULATION_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + GROUPS_ID + " integer, "
            + ID_WHO + " integer, "
            + NAME_WHO + " char(70), "
            + ID_WHOM + " integer, "
            + NAME_WHOM + " char(70), "
            + SUMMA + " integer, "
            + IS_DELETED + " integer "
            + ")";

}