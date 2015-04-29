package com.beautyteam.everpay.REST.Processors;

import android.content.Intent;

import com.beautyteam.everpay.Constants;

/**
 * Created by Admin on 29.04.2015.
 */
public class ProcessorFactory {
    public static Processor getProcessor(Intent intent) {
        String action = intent.getAction();
        if (Constants.Action.GET_GROUPS.equals(action)) {
            return new GetProcessors();
        } else
        if (Constants.Action.INIT_VK_USERS.equals(action)) {
            return new VKProcessor();
        }
        return null;
    }
}
