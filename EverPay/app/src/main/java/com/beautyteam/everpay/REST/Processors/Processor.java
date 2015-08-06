package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.Utils.DateFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 29.04.2015.
 */
public abstract class Processor {
    public abstract void request(Intent intent, Service service);
    protected SharedPreferences sPref;

    public Processor(Context context) {
        sPref = context.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
    }

    public void updateDateInGroup(int groupId, Context context) {
        // Обновим дату в группе
        ContentValues cv = new ContentValues();
        cv.put(Groups.UPDATE_TIME, getAndIncrementMaxDate(context));
        context.getContentResolver().update(EverContentProvider.GROUPS_CONTENT_URI, cv, Groups.GROUP_ID + "=" + groupId, null);
    }

    /*
    Выбираем максимальное время, увеличиваем на 1 секунду и возвращаем в виде текста
     */
    protected String getAndIncrementMaxDate(Context context) {

        Cursor maxCursor = context.getContentResolver().query(EverContentProvider.GROUPS_CONTENT_URI, new String [] {"MAX("+Groups.UPDATE_TIME+")"}, null, null, null);
        maxCursor.moveToFirst();
        String max = maxCursor.getString(0);

        if (max == null) {
            Date cDate = new Date();
            max = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cDate);
        }

        int lastInt = max.lastIndexOf(":");
        String lastChar = max.substring(lastInt+1);
        int lastDigit = Integer.parseInt(lastChar);
        lastDigit++;

        String lastDigitInString = lastDigit + "";

        if (lastDigit<10) {
            lastDigitInString = "0" + lastDigitInString;
        }

        max = max.substring(0, lastInt+1) + lastDigitInString;
        return max;
    }

    protected int getUserId() {
        return sPref.getInt(Constants.Preference.USER_ID, 0);
    }

    protected int getUserVkId() {
        return sPref.getInt(Constants.Preference.USER_ID_VK, 0);
    }

    protected String getAccessToken() {
        return sPref.getString(Constants.Preference.ACCESS_TOKEN, "0");
    }


    protected ContentValues readHistory(JSONObject history) {
        try {
            // ===================
            ContentValues cv = new ContentValues();
            cv.put(History.NEWS_ID, history.getString("news_id"));
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
            String formatedDate = DateFormatter.formatDateTime(date);
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

    public String urlConnectionPost(String strUrl, String urlParameters) {
        HttpURLConnection connection = null;
        String str = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(urlParameters);
            writer.flush();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream in = connection.getInputStream();
                str = handleInputStream(in);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return str;
    }

    private String handleInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = "", line = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        Log.e("", result);
        return result;
    }
}
