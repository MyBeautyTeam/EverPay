package com.beautyteam.everpay.Database;

/**
 * Created by popka on 06.08.15.
 */
public class CalculationDetails {
    static final String CALCULATION_DETAILS_TABLE = "calculation_details";

    static public final String ITEM_ID = "_id";
    static public final String GROUP_ID = "group_id";
    static public final String BILL_TITLE = "bill_title";
    static public final String NEED_SUM = "need";
    static public final String INVEST_SUM = "invest";
    static public final String BALANCE = "balance";

    static public final String CREATE_TABLE = "create table " + CALCULATION_DETAILS_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + GROUP_ID + " integer, "
            + BILL_TITLE + " CHAR(50), "
            + NEED_SUM + " integer, "
            + INVEST_SUM + " integer, "
            + BALANCE + " integer"
            + ")";


}
