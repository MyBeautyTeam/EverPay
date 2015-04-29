package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.REST.Service;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import static com.beautyteam.everpay.Constants.Action.GET_GROUPS;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by Admin on 29.04.2015.
 */
public class GetProcessors extends Processor {

    @Override
    public void request(Intent intent, Service service) {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        Log.d(Constants.LOG, "Processor, request()");
        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE
        String action = intent.getAction();

        if (GET_GROUPS.equals(action)) {
            SharedPreferences sPref = service.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
            params.add(new BasicNameValuePair("users_id", 8 + ""/*sPref.getString(USER_ID, "0")*/));
            params.add(new BasicNameValuePair("access_token", sPref.getString(ACCESS_TOKEN, "0")));
            String response = get(Constants.URL.GET_GROUPS, params);
            if (response != null) {
                if (response.contains("200")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("response");
                        jsonObject = jsonObject.getJSONObject("groups");

                        ContentValues cv;
                        JSONObject group;
                        for (int i = 0; (group = jsonObject.getJSONObject(i + "")) != null; i++) {
                            cv = new ContentValues();
                            cv.put(Groups.GROUP_ID, group.getString("groups_id"));
                            cv.put(Groups.TITLE, group.getString("title"));
                            //cv.put(Groups.UPDATE_TIME, group.getString("update_datetime"));
                            if (group.getBoolean("is_calculated"))
                                cv.put(Groups.IS_CALCULATED, 1);
                            else
                                cv.put(Groups.IS_CALCULATED, 0);
                            service.getContentResolver().insert(EverContentProvider.GROUPS_CONTENT_URI, cv);
                        }
                        result = Constants.Result.OK;
                    } catch (JSONException e) {
                        result = Constants.Result.ERROR;
                    }
                } else {
                    result = Constants.Result.ERROR;
                }
            }
        } else if (false) {

        }

        service.onRequestEnd(result, intent);

    }

    private String get(String stringUrl, LinkedList<NameValuePair> params) {

        Log.d(Constants.LOG, "Processor, request()");

        String paramString = URLEncodedUtils.format(params, "utf-8");
        if (!stringUrl.endsWith("?"))
            stringUrl += "?";
        stringUrl += paramString;

        HttpClient httpClient = new DefaultHttpClient(); // должен быть один в классе или несколько???
        HttpGet request = new HttpGet(stringUrl);

        try {
            HttpResponse response = httpClient.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String result = "";
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            Log.d("MyProgram", "result = " + result);
            return result;
        } catch (IOException e) {
            return null;
        }

    }


}
