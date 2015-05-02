package com.beautyteam.everpay.REST.Processors;

import android.content.Intent;

import com.beautyteam.everpay.Constants;

/**
 * Created by Admin on 29.04.2015.
 */
public class ProcessorFactory {
    public static Processor getProcessor(Intent intent) {
        String action = intent.getAction();
        if      (
                Constants.Action.GET_GROUPS.equals(action) ||
                Constants.Action.GET_GROUP_MEMBERS.equals(action) ||
                Constants.Action.GET_DEBTS.equals(action) ||
                Constants.Action.GET_BILL.equals(action) ||
                Constants.Action.GET_HISTORY.equals(action)
                ) {
            return new GetProcessors();
        } else
        if (Constants.Action.INIT_VK_USERS.equals(action)) {
            return new VKProcessor();
        } else
        if (
            Constants.Action.ADD_BILL.equals(action) ||
            Constants.Action.ADD_MEMBER_TO_GROUP.equals(action) ||
            Constants.Action.REMOVE_MEMBER_FROM_GROUP.equals(action) ||
            Constants.Action.ADD_GROUP.equals(action) ||
            Constants.Action.CALCULATE.equals(action)
            ) {
            return new PostProcessor();
        } else
        if (
            Constants.Action.EDIT_BILL.equals(action)
            )
            return new PutProcessor();

        return null;
    }
}
