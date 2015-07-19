package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 07.04.2015.
 */
public class Debts {
    // Таблица
    static public final String DEBTS_TABLE = "debts";

    // Поля
    static public final String ITEM_ID = "_id";
    static public final String SUMMA = "summa";
    static public final String USER_ID = "user_id";
    static public final String USER_VK_ID = "user_vk_id";
    static public final String USER_NAME = "user_name";
    static public final String GROUP_TITLE = "group_title";
    static public final String GROUP_ID = "GROUP_ID";
    static public final String IS_I_DEBT = "is_i_debt";
    public static final String STATE = "state";
    public static final String RESULT = "result";

    // Агрегаторные функции
    static public final String SUM_SUMMA = "SUM(summa)";


    // Скрипт создания таблицы
    static public final String CREATE_TABLE = "create table " + DEBTS_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + USER_ID + " integer, "
            + USER_VK_ID + " integer, "
            + USER_NAME + " CHAR(70), "
            + GROUP_ID + " integer, "
            + GROUP_TITLE + " CHAR(50), "
            + SUMMA + " integer, "
            + IS_I_DEBT + " integer, "
            + STATE + " integer, "
            + RESULT + " integer"
            + ")";

}