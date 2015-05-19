package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.Utils.DateFormetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 29.04.2015.
 */
public abstract class Processor {
    public abstract void request(Intent intent, Service service);

    public void updateDateInGroup(int groupId, Context context) {
        // Обновим дату в группе
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
        ContentValues cv = new ContentValues();
        cv.put(Groups.UPDATE_TIME, fDate);
        context.getContentResolver().update(EverContentProvider.GROUPS_CONTENT_URI, cv, Groups.GROUP_ID + "=" + groupId, null);
    }


    public ContentValues readHistory(JSONObject history) {
        try {
            // ===================
            ContentValues cv = new ContentValues();
            try {
                cv.put(History.USERS_ID_WHO_SAY, history.getString("users_id_who_say"));
            } catch (JSONException e) {
            }
            cv.put(History.USERS_ID_WHO, history.getString("users_id_who"));
            try {
                cv.put(History.USERS_ID_WHOM, history.getString("users_id_whom"));
            } catch (JSONException e) {
            }

            cv.put(History.GROUP_ID, history.getString("groups_id"));
            try {
                cv.put(History.BILL_ID, history.getString("bills_id"));
            } catch (JSONException e) {
            }

            try {
                cv.put(History.EDITED_BILL_ID, history.getString("edited_bills_id"));
            } catch (JSONException e) {
            }

            try {
                cv.put(History.DEBTS_ID, history.getString("debts_id"));
            } catch (JSONException e) {
            }


            cv.put(History.ACTION, history.getString("action"));

            String date = history.getString("action_datetime");
            String formatedDate = DateFormetter.formatDateTime(date);
            cv.put(History.ACTION_DATETIME, formatedDate);
            try {
                cv.put(History.TEXT_WHO_SAY, history.getString("text_who_say"));
            } catch (JSONException e) {
            }

            try {
                cv.put(History.TEXT_SAY, history.getString("text_say"));
            } catch (JSONException e) {
            }
            cv.put(History.TEXT_WHO, history.getString("text_who"));
            cv.put(History.TEXT_DESCRIPTION, history.getString("text_description"));
            cv.put(History.TEXT_WHAT_WHOM, history.getString("text_what_whom"));

            cv.put(History.STATE, Constants.State.ENDS);
            cv.put(History.RESULT, Constants.Result.OK);
            return cv;
        } catch (JSONException e) {
            return null;
        }
    }
}
