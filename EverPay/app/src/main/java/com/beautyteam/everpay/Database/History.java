package com.beautyteam.everpay.Database;

/**
 * Created by Admin on 29.04.2015.
 */
public class History {
    static final String HISTORY_TABLE = "history";

    static public final String ITEM_ID = "_id";
    static public final String WHO_SAY_ID = "id_who_say";
    static public final String WHO_ID = "id_who";
    static public final String WHOM_ID = "id_whom";
    static public final String GROUP_ID = "group_id";
    static public final String BILL_ID = "bill_id";
    static public final String EDITED_BILL_ID = "edited_bills_id";	// null=True
    static public final String DEBTS_ID = "debts_id";	// null=True
    static public final String ACTION = "action";
    static public final String TIME = "action_datetime";
    static public final String TEXT_WHO_SAY = "text_who_say";
    static public final String TEXT_SAY = "text_say";
    static public final String TEXT_WHO = "text_who";
    static public final String TEXT_DESCRIPTION ="text_description";
    static public final String TEXT_WHAT_WHOM ="text_description";

    static public final String CREATE_TABLE = "create table " + HISTORY_TABLE + "("
            + ITEM_ID + " integer primary key autoincrement, "
            + WHO_SAY_ID + " integer, "
            + WHO_ID + " integer, "
            + WHOM_ID + " integer, "
            + GROUP_ID + " integer, "
            + BILL_ID + " integer, "
            + EDITED_BILL_ID + " integer, "
            + DEBTS_ID + " integer, "
            + ACTION + " integer, "
            + TIME + " integer, "
            + TEXT_WHO_SAY + " char(70), "
            + TEXT_SAY + " char(70), "
            + TEXT_WHO + " char(70), "
            + TEXT_DESCRIPTION + " char(200), "
            + TEXT_WHAT_WHOM + " char(200) "
            + ")";

}
