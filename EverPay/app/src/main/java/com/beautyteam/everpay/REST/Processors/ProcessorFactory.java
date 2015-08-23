package com.beautyteam.everpay.REST.Processors;

import android.content.Context;
import android.content.Intent;

import static com.beautyteam.everpay.Constants.Action.*;

/**
 * Created by Admin on 29.04.2015.
 */
public class ProcessorFactory {
    public static Processor getProcessor(Context context, Intent intent) {
        String action = intent.getAction();
        if      (
                GET_GROUPS.equals(action) ||
                GET_GROUP_MEMBERS.equals(action) ||
                GET_DEBTS.equals(action) ||
                GET_BILL.equals(action) ||
                GET_HISTORY.equals(action) ||
                GET_CALC_DETAILS.equals(action)
                ) {
            return new GetProcessors(context);
        } else
        if (INIT_VK_USERS.equals(action)) {
            return new VKAuthProcessor(context);
        } else
        if (
            ADD_BILL.equals(action) ||
            ADD_MEMBER_TO_GROUP.equals(action) ||
            ADD_GROUP.equals(action) ||
            CALCULATE.equals(action) ||
            CREATE_USER.equals(action) ||
            CREATE_AND_ADD_USER.equals(action) ||
            REGISTER_GCM.equals(action) ||
            PUSH_IN_APP.equals(action)
            ) {
            return new PostProcessor(context);
        } else
        if (
            EDIT_BILL.equals(action) ||
            EDIT_GROUP.equals(action) ||
            EDIT_CALCULATION.equals(action)
            ) {
            return new PutProcessor(context);
        } else
        if (
            REMOVE_MEMBER_FROM_GROUP.equals(action) ||
            REMOVE_BILL.equals(action) ||
            UNREGISTER_GCM.equals(action)
            )
            return new DeleteProcessor(context);
        else
        if (SEND_MESSAGE_WITH_IMAGE.equals(action)) {
            return new VKProcessor(context);
        }
        else
        if (BUG_REPORT.equals(action)) {
            return new EmailerProcessor(context);
        }

        return null;
    }
}
